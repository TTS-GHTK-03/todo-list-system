package org.ghtk.todo_list.facade;

import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.dto.request.*;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.*;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.OtpService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.util.CryptUtil;

import static org.ghtk.todo_list.constant.CacheConstant.*;

@Slf4j
@RequiredArgsConstructor
public class AuthFacadeServiceImpl implements AuthFacadeService {

    private final AuthAccountService authAccountService;
    private final AuthUserService authUserService;
    private final OtpService otpService;
    private final RedisCacheService redisCacheService;
    private final EmailHelper emailHelper;


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
        var authAccount = authAccountService.create(
                request.getUsername(),
                CryptUtil.getPasswordEncoder().encode(request.getPassword())
        );
        var authUser = authUserService.create(request.getEmail(), authAccount.getId());

        var otp = otpService.generateOtp();
        var redisKey = request.getEmail() + OTP_ACTIVE_ACCOUNT_KEY;
        redisCacheService.save(redisKey, otp, OTP_TTL_MINUTES, TimeUnit.MINUTES);

        String subject = "Your OTP for account activation";
        var param = new HashMap<String, Object>();
        param.put("otp", otp);
        param.put("otp_life", String.valueOf(OTP_TTL_MINUTES));
        emailHelper.send(subject, request.getEmail(), "OTP-template", param);

    }

    @Override
    public void activeAccount(ActiveAccountRequest request) {
        log.info("(activeAccount)email: {} otp: {}", request.getEmail(), request.getOtp());
        var account = authAccountService
                .findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("(activeAccount)email not found : {}", request.getEmail());
                    throw new EmailNotFoundException(request.getEmail());
                });
        var redisKey = request.getEmail() + OTP_ACTIVE_ACCOUNT_KEY;
        var otpCacheOptional = redisCacheService.get(redisKey);
        if (otpCacheOptional.isEmpty()) {
            log.error("(invoke)otpCache is null for email: {}", request.getEmail());
            throw new OTPNotFoundException(request.getEmail());
        }
        var otpCache = String.valueOf(otpCacheOptional.get());
        if (!Objects.equals(otpCache, request.getOtp())) {
            log.error("(invoke)otp : {}, otpCache : {}", request.getOtp(), otpCache);
            throw new OTPInvalidException(request.getOtp());
        }

        account.setIsActivated(true);
        authAccountService.save(account);
        redisCacheService.delete(redisKey);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("(forgotPassword)request: {}", request);
        if (!authUserService.existsByEmail(request.getEmail())) {
            log.error("(forgotPassword)email: {}", request.getEmail());
            throw new EmailNotFoundException(request.getEmail());
        }
        var otp = otpService.generateOtp();
        var redisKey = request.getEmail() + RESET_PASSWORD_OTP_KEY;
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
    public void resetPassword(ResetPasswordRequest request) {
        log.info("(resetPassword)request: {}", request);

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.error("(resetPassword) Password and confirmation password do not match: {} {}", request.getPassword(),
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

    private String generateResetPasswordKey(String email) {
        return Base64.getEncoder().encodeToString((email + System.currentTimeMillis()).getBytes());
    }

}
