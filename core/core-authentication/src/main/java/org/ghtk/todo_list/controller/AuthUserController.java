package org.ghtk.todo_list.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.VerifyEmailRequest;
import org.ghtk.todo_list.dto.request.VerifyRegisterRequest;
import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.LoginRequest;
import org.ghtk.todo_list.dto.request.VerifyResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.AuthFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthUserController {

  private final AuthFacadeService authFacadeService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse register(@RequestBody @Valid RegisterRequest request) {
    log.info("(register)request: {}", request);
    authFacadeService.register(request);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
        "Register success");
  }

  @PostMapping("/register/otp/validate")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse verifyRegister(@RequestBody @Valid VerifyRegisterRequest request) {
    log.info("(verifyRegister)email: {}, otp: {}", request.getEmail(), request.getOtp());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authFacadeService.verifyRegister(request));
  }

  @PostMapping("/register/email/validate")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
    log.info("(verifyEmail)email: {}", request.getEmail());
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
        authFacadeService.verifyEmail(request));
  }

  @PostMapping("/forgot")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
    log.info("(forgotPassword)request: {}", request);
    authFacadeService.forgotPassword(request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
            "An OTP for password reset has been sent to your email address. " +
                    "Please check your inbox to proceed with resetting your password.");
  }

  @PostMapping("/reset-password/otp/validate")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse verifyResetPassword(@RequestBody @Valid VerifyResetPasswordRequest request) {
    log.info("(verifyResetPassword)request: {}", request);

    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
            authFacadeService.verifyResetPassword(request));
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse login(@Valid @RequestBody LoginRequest request) {
    log.info("(login)request: {}", request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),authFacadeService.login(request));
  }

}
