package org.ghtk.todo_list.facade;

import static org.ghtk.todo_list.constant.CacheConstant.OTP_ACTIVE_ACCOUNT_KEY;
import static org.ghtk.todo_list.constant.CacheConstant.OTP_TTL_MINUTES;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.dto.request.ActiveAccountRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.exception.EmailNotFoundException;
import org.ghtk.todo_list.exception.OTPInvalidException;
import org.ghtk.todo_list.exception.OTPNotFoundException;
import org.ghtk.todo_list.exception.PasswordConfirmNotMatchException;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.OtpService;
import org.ghtk.todo_list.service.RedisCacheService;
import org.ghtk.todo_list.util.CryptUtil;

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
    var authUser = authUserService.create(request.getEmail(), authAccount. getId());

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
}
