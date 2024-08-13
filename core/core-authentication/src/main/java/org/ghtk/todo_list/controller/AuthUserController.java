package org.ghtk.todo_list.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RegisterResponse;
import org.ghtk.todo_list.dto.request.VerifyEmailRequest;
import org.ghtk.todo_list.dto.request.VerifyRegisterRequest;
import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.LoginRequest;
import org.ghtk.todo_list.dto.request.VerifyResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.request.*;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.dto.response.LoginResponse;
import org.ghtk.todo_list.dto.response.VerifyRegisterResponse;
import org.ghtk.todo_list.dto.response.VerifyResetPasswordResponse;
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
@Tag(name = "Authentication", description = "Authentication API")
public class AuthUserController {

  private final AuthFacadeService authFacadeService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Register")
  public BaseResponse<String> register(@RequestBody @Valid RegisterRequest request) {
    log.info("(register)request: {}", request);
    authFacadeService.register(request);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
        "Register success");
  }

  @PostMapping("/register/otp/validate")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Verify register")
  public BaseResponse<VerifyRegisterResponse> verifyRegister(@RequestBody @Valid VerifyRegisterRequest request) {
    log.info("(verifyRegister)email: {}, otp: {}", request.getEmail(), request.getOtp());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authFacadeService.verifyRegister(request));
  }

  @PostMapping("/register/email/validate")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Verify email")
  public BaseResponse<String> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
    log.info("(verifyEmail)email: {}", request.getEmail());
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
        authFacadeService.verifyEmail(request));
  }

  @PostMapping("/forgot")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Forgot password")
  public BaseResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
    log.info("(forgotPassword)request: {}", request);
    authFacadeService.forgotPassword(request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        "An OTP for password reset has been sent to your email address. " +
            "Please check your inbox to proceed with resetting your password.");
  }

  @PostMapping("/reset-password/otp/validate")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Verify reset password")
  public BaseResponse<VerifyResetPasswordResponse> verifyResetPassword(@RequestBody @Valid VerifyResetPasswordRequest request) {
    log.info("(verifyResetPassword)request: {}", request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authFacadeService.verifyResetPassword(request));
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Login")
  public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    log.info("(login)request: {}", request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authFacadeService.login(request));
  }

  @PostMapping("/reset-password")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Reset password")
  public BaseResponse<String> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    log.info("(resetPassword)request: {}", request);
    authFacadeService.resetPassword(request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        "Reset Password successfully!!");
  }

  @PostMapping("/resend/otp")
  @ResponseStatus(HttpStatus.OK)
  @Operation(description = "Resend otp")
  public BaseResponse<String> resendOtp(@RequestBody @Valid ResendOtpRequest request) {
    log.info("(resendOtp)request: {}", request);
    authFacadeService.resendOtp(request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        "resend Otp successfully!!");
  }

}
