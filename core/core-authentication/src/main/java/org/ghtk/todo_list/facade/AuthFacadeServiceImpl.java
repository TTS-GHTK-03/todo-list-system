package org.ghtk.todo_list.facade;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.AccountLockedTime;
import org.ghtk.todo_list.constant.RegisterResponse;
import org.ghtk.todo_list.constant.ResendOtpType;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.dto.request.VerifyEmailRequest;
import org.ghtk.todo_list.dto.request.VerifyRegisterRequest;
import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.LoginRequest;
import org.ghtk.todo_list.dto.request.VerifyResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.ActiveLoginResponse;
import org.ghtk.todo_list.dto.response.LoginResponse;
import org.ghtk.todo_list.dto.response.UnactiveLoginResponse;
import org.ghtk.todo_list.dto.response.VerifyRegisterResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.AccountLockedException;
import org.ghtk.todo_list.exception.EmailNotFoundException;
import org.ghtk.todo_list.exception.OTPInvalidException;
import org.ghtk.todo_list.dto.request.*;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.exception.*;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;
import org.ghtk.todo_list.exception.OTPNotFoundException;
import org.ghtk.todo_list.exception.PasswordConfirmNotMatchException;
import org.ghtk.todo_list.exception.RegisterKeyNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.exception.UsernameNotFoundException;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthTokenService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.OtpService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.util.CryptUtil;

import static org.ghtk.todo_list.constant.CacheConstant.*;
import static org.ghtk.todo_list.constant.RegisterResponse.*;

@Slf4j
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService {

  private final AuthAccountService authAccountService;
  private final AuthUserService authUserService;
  private final OtpService otpService;
  private final RedisCacheService redisCacheService;
  private final EmailHelper emailHelper;
  private final AuthTokenService authTokenService;


  @Override
  public void register(RegisterRequest request) {
    log.info("(register)request: {}", request);
    if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
      log.error(
          "(register)password: {}, confirmPassword:{}  don't match",
          request.getPassword(),
          request.getConfirmPassword());
      throw new PasswordConfirmNotMatchException();
    }

    var key = redisCacheService.get(REGISTER_SHARE_KEY, request.getEmail());
    var registerKeyCache = redisCacheService.get(REGISTER_KEY, request.getEmail());
    if (key.isEmpty() || !key.get().equals(INACTIVE)) {

      log.error("(registerKeyCache) registerKeyCache: {}", registerKeyCache);

      if (registerKeyCache.isEmpty() || !registerKeyCache.get().equals(request.getRegisterKey())) {
        log.error("(register) RegisterKey not found for email: {}", request.getEmail());
        throw new RegisterKeyNotFoundException(request.getEmail());
      }
      var authAccount = authAccountService.create(
          request.getUsername(),
          CryptUtil.getPasswordEncoder().encode(request.getPassword())
      );
      authUserService.create(request.getEmail(), authAccount.getId());
      redisCacheService.delete(REGISTER_KEY, request.getEmail());
    } else {
      var authAccount = authAccountService.create(
          request.getUsername(),
          CryptUtil.getPasswordEncoder().encode(request.getPassword())
      );
      authUserService.saveUserShare(request.getEmail(), authAccount.getId());
      redisCacheService.delete(REGISTER_SHARE_KEY, request.getEmail());
    }
  }

  @Override
  public VerifyRegisterResponse verifyRegister(VerifyRegisterRequest request) {
    log.info("(verifyRegister)email: {} otp: {}", request.getEmail(), request.getOtp());
    var redisKey = request.getEmail() + OTP_VERIFICATION_ACCOUNT_KEY;
    var otpCacheOptional = redisCacheService.get(redisKey);
    if (otpCacheOptional.isEmpty()) {
      log.error("(verifyRegister)otpCache is null for email: {}", request.getEmail());
      throw new OTPNotFoundException(request.getEmail());
    }
    var otpCache = String.valueOf(otpCacheOptional.get());
    if (!Objects.equals(otpCache, request.getOtp())) {
      log.error("(verifyRegister)otp : {}, otpCache : {}", request.getOtp(), otpCache);
      throw new OTPInvalidException(request.getOtp());
    }
    redisCacheService.delete(redisKey);

    var registerRedisKey = generateResetPasswordKey(request.getEmail());
    redisCacheService.save(REGISTER_KEY, request.getEmail(), registerRedisKey);

    return VerifyRegisterResponse.builder()
        .registerKey(registerRedisKey)
        .build();
  }

  @Override
  public void forgotPassword(ForgotPasswordRequest request) {
    log.info("(forgotPassword)request: {}", request);
    if (!authUserService.existsByEmail(request.getEmail())) {
      log.error("(forgotPassword)email: {}", request.getEmail());
      throw new EmailNotFoundException(request.getEmail());
    }
    var redisKey = request.getEmail() + RESET_PASSWORD_OTP_KEY;
    var otpCache = redisCacheService.get(redisKey);
    if (otpCache.isPresent()) {
      log.error("(forgotPassword)otpCache is still valid for email: {}", request.getEmail());
      throw new OtpStillValidException(request.getEmail());
    }
    var otp = otpService.generateOtp();
    redisCacheService.save(redisKey, otp, OTP_TTL_MINUTES, TimeUnit.MINUTES);

    String subject = "Your OTP for rest password";
    var param = new HashMap<String, Object>();
    param.put("otp", otp);
    param.put("otp_life", String.valueOf(OTP_TTL_MINUTES));
    emailHelper.send(subject, request.getEmail(), "OTP-template", param);

  }

  @Override
  public VerifyResetPasswordResponse verifyResetPassword(VerifyResetPasswordRequest request) {
    log.info("(verifyResetPassword)request: {}", request);
    if (!authUserService.existsByEmail(request.getEmail())) {
      log.error("(verifyResetPassword)email: {}", request.getEmail());
      throw new EmailNotFoundException(request.getEmail());
    }

    var redisKey = request.getEmail() + RESET_PASSWORD_OTP_KEY;
    var cachedOtp = redisCacheService.get(redisKey);
    log.error("(verifyResetPassword)otpCache: {}", cachedOtp);

    if (cachedOtp.isEmpty() || !cachedOtp.get().equals(request.getOtp())) {
      log.error("(verifyResetPassword) OTP not found for email: {}", request.getEmail());
      throw new OTPNotFoundException(request.getEmail());
    }

    log.info("(verifyResetPassword) OTP validated successfully for email: {}", request.getEmail());
    redisCacheService.delete(redisKey);

    var resetPasswordKeyRedisKey = generateResetPasswordKey(request.getEmail());
    redisCacheService.save(RESET_PASSWORD_KEY, request.getEmail(), resetPasswordKeyRedisKey);

    return VerifyResetPasswordResponse.builder()
        .resetPasswordKey(resetPasswordKeyRedisKey)
        .build();
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    log.info("(invoke)username : {}, password : {}", request.getUsername(), request.getPassword());
    var account = authAccountService.findByUsername(request.getUsername())
        .orElseThrow(() -> {
          log.error("Customer have username {} not found", request.getUsername());
          return new UsernameNotFoundException(request.getUsername());
        });
    if (Boolean.TRUE.equals(account.getIsLockedPermanent())) {
      log.error("(invoke)account has username : {} is locked permanently", account.getUsername());
      throw new AccountLockedException();
    }

    if (Boolean.FALSE.equals(account.getIsActivated())) {
      log.warn("(invoke)account has username : {} is not actived", account.getUsername());
      return new UnactiveLoginResponse("Your account is not activated");
    }

    var user = authUserService.findByAccountId(account.getId())
        .orElseThrow(() -> {
          log.error("Customer have account id : {} not found", account.getId());
          return new UserNotFoundException();
        });

    Long unlockLoginTime = redisCacheService.getOrDefault(LOGIN_UNLOCK_TIME_KEY,
        user.getEmail(), 0L);
    if (Instant.now().getEpochSecond() < unlockLoginTime) {
      log.error("(invoke)account has username : {} is locked temporary", account.getUsername());
      throw new AccountLockedException();
    }
    if (!CryptUtil.getPasswordEncoder().matches(request.getPassword(), account.getPassword())) {
      handleFailedAttempt(user.getEmail(), account.getId());
      log.error("(invoke)account has username : {} is have wrong password", account.getUsername());
      throw new PasswordIncorrectException();
    }

    redisCacheService.delete(LOGIN_UNLOCK_TIME_KEY, user.getEmail());
    redisCacheService.delete(LOGIN_FAILED_ATTEMPT_KEY, user.getEmail());

    return createLoginResponse(user, account);
  }

  @Override
  public String verifyEmail(VerifyEmailRequest request) {
    log.info("(verifyEmail)email: {}", request.getEmail());
    if (!authUserService.existsByEmail(request.getEmail())) {
      log.info("(verifyEmail)email don't registered: {}", request.getEmail());

      var redisKey = request.getEmail() + OTP_VERIFICATION_ACCOUNT_KEY;
      var otpCache = redisCacheService.get(redisKey);
      if (otpCache.isPresent()) {
        log.error("(verifyEmail)otpCache is still valid for email: {}", request.getEmail());
        throw new OtpStillValidException(request.getEmail());
      }
      var otp = otpService.generateOtp();
      redisCacheService.save(redisKey, otp, OTP_TTL_MINUTES, TimeUnit.MINUTES);

      String subject = "Your OTP for account verification";
      var param = new HashMap<String, Object>();
      param.put("otp", otp);
      param.put("otp_life", String.valueOf(OTP_TTL_MINUTES));
      emailHelper.send(subject, request.getEmail(), "OTP-template", param);
      return UNREGISTERED;
    } else {
      log.info("(verifyEmail)email has been registered: {}", request.getEmail());
      if (authUserService.existsByEmailAndAccountId(request.getEmail(), null)) {
        redisCacheService.save(REGISTER_SHARE_KEY, request.getEmail(), INACTIVE);
        return INACTIVE;
      } else {
        return ACTIVE;
      }
    }
  }

  private void handleFailedAttempt(String email, String accountId) {
    log.info("(handleFailedAttempt) email: {}", email);
    Integer attempts = redisCacheService.getOrDefault(LOGIN_FAILED_ATTEMPT_KEY, email,
        0);
    attempts++;
    redisCacheService.save(LOGIN_FAILED_ATTEMPT_KEY, email, attempts);

    if (attempts.equals(AccountLockedTime.FIVE.getAttempts())) {
      redisCacheService.save(LOGIN_UNLOCK_TIME_KEY,
          email, Instant.now().getEpochSecond() + AccountLockedTime.FIVE.getCooldownTime());
      log.info("Account locked for 5 failed attempts for email: {}", email);
    }
    if (attempts.equals(AccountLockedTime.TEN.getAttempts())) {
      redisCacheService.save(LOGIN_UNLOCK_TIME_KEY,
          email, Instant.now().getEpochSecond() + AccountLockedTime.TEN.getCooldownTime());
      log.info("Account locked for 10 failed attempts for email: {}", email);
    }
    if (attempts.equals(AccountLockedTime.FIFTEEN.getAttempts())) {
      authAccountService.updateLockPermanentById(accountId, true);
      redisCacheService.delete(LOGIN_UNLOCK_TIME_KEY, email);
      redisCacheService.delete(LOGIN_FAILED_ATTEMPT_KEY, email);
      log.info("Account locked for 15 failed attempts for email: {}", email);
    }
    log.info("(handleFailedAttempt)Customer have email : {} have {} failed attempts", email,
        attempts);
  }

  @Override
  public void resetPassword(ResetPasswordRequest request) {
    log.info("(resetPassword)request: {}", request);

    if (!request.getPassword().equals(request.getConfirmPassword())) {
      log.error("(resetPassword) Password and confirmation password do not match: {} {}",
          request.getPassword(),
          request.getConfirmPassword());
      throw new PasswordConfirmNotMatchException();
    }

    if (!authUserService.existsByEmail(request.getEmail())) {
      log.error("(resetPassword)email: {}", request.getEmail());
      throw new EmailNotFoundException(request.getEmail());
    }

    var resetPasswordKey = redisCacheService.get(RESET_PASSWORD_KEY, request.getEmail());
    if (resetPasswordKey.isEmpty() ||
        !resetPasswordKey.get().equals(request.getResetPasswordKey())) {
      log.error("(resetPassword) Reset Password Key not found: {}", request.getResetPasswordKey());
      throw new ResetPasswordKeyNotFoundException();
    }

    redisCacheService.delete(RESET_PASSWORD_KEY, request.getEmail());

    AuthAccount account = authAccountService
        .findByEmail(request.getEmail())
        .orElseThrow(() -> {
          log.error("(resetPassword)email not found : {}", request.getEmail());
          throw new EmailNotFoundException(request.getEmail());
        });

    account.setPassword(CryptUtil.getPasswordEncoder().encode(request.getPassword()));

    authAccountService.save(account);

    log.info("(resetPassword) Password reset successfully for email: {}", request.getEmail());
  }

  @Override
  public void changePassword(ChangePasswordRequest request, String userId) {

    log.info("(changePassword)request: {}, userId: {}", request, userId);

    if (!request.getConfirmPassword().equals(request.getNewPassword())) {
      log.error("(changePassword) New password and confirmation password do not match: {}, {}",
          request.getNewPassword(),
          request.getConfirmPassword());
      throw new PasswordConfirmNotMatchException();
    }

    if (request.getOldPassword().equals(request.getNewPassword())) {
      log.error("(changePassword) New password and Old password are the same: {}, {}",
          request.getNewPassword(), request.getOldPassword());
      throw new PasswordSimilarException();
    }

    AuthAccount account = authAccountService.findByUserIdWithThrow(userId);

    if (!CryptUtil.getPasswordEncoder().matches(request.getOldPassword(), account.getPassword())) {
      log.error("(changePassword) Your password is incorrect: {}",
          request.getOldPassword());
      throw new PasswordIncorrectException();
    }

    if (CryptUtil.getPasswordEncoder().matches(request.getNewPassword(), account.getPassword())) {
      log.error("(changePassword) New password and password in database are the same: {}",
          request.getNewPassword());
      throw new PasswordSimilarException();
    }

    account.setPassword(CryptUtil.getPasswordEncoder().encode(request.getNewPassword()));
    authAccountService.save(account);
    log.info("(changePassword) Password change successfully!!");
  }

  @Override
  public void resendOtp(ResendOtpRequest request) {
    log.info("(resendOtp)request: {}", request);

    if (!authUserService.existsByEmail(request.getEmail())) {
      log.error("(resendOtp)email: {}", request.getEmail());
      throw new EmailNotFoundException(request.getEmail());
    }

    String type = request.getType().trim().toUpperCase();

    if (type.equals(ResendOtpType.FORGOT.toString())) {
      log.info("(resendOtp) come forgot");
      resend(request.getEmail(), RESET_PASSWORD_OTP_KEY);
    } else if (type.equals(ResendOtpType.REGISTER.toString())) {
      log.info("(resendOtp) come register");
      resend(request.getEmail(), REGISTER_KEY);
    } else {
      log.error("(resendOtp) Invalid resend type {} value", request.getType());
      throw new TypeResendInvalidException(request.getType());
    }
  }

  private void resend(String email, String otpKey) {
    log.info("(resend)email {}, otpKey {}", email, otpKey);
    var redisKey = email + otpKey;

    var otpCache = redisCacheService.get(redisKey);
    if (otpCache.isPresent()) {
      log.error("(verifyEmail)otpCache is still valid for email: {}", email);
      throw new OtpStillValidException(email);
    }

    var otp = otpService.generateOtp();
    redisCacheService.save(redisKey, otp, OTP_TTL_MINUTES, TimeUnit.MINUTES);

    String subject = "Your OTP for password reset";
    var param = new HashMap<String, Object>();
    param.put("otp", otp);
    param.put("otp_life", String.valueOf(OTP_TTL_MINUTES));
    emailHelper.send(subject, email, "OTP-template", param);
  }

  private String generateResetPasswordKey(String email) {
    return Base64.getEncoder().encodeToString((email + System.currentTimeMillis()).getBytes());
  }

  private LoginResponse createLoginResponse(AuthUser user, AuthAccount account) {
    ActiveLoginResponse loginResponse = new ActiveLoginResponse();
    loginResponse.setAccessToken(
        authTokenService.generateAccessToken(user.getId(), user.getEmail(),
            account.getUsername()));
    loginResponse.setRefreshToken(
        authTokenService.generateRefreshToken(user.getId(), user.getEmail(),
            account.getUsername()));
    loginResponse.setAccessTokenLifeTime(authTokenService.getAccessTokenLifeTime());
    loginResponse.setRefreshTokenLifeTime(authTokenService.getRefreshTokenLifeTime());
    return loginResponse;
  }

}
