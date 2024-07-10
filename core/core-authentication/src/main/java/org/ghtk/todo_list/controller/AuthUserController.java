package org.ghtk.todo_list.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.ForgotPasswordRequest;
import org.ghtk.todo_list.dto.request.OTPResetPasswordRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.AuthFacadeService;
import org.ghtk.todo_list.service.AuthUserService;
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
        "Register success and otp to activate has been sent to the email");
  }

  @PostMapping("/forgot")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
    log.info("(register)request: {}", request);
    authFacadeService.forgotPassword(request);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
            "Otp to activate has been sent to the email");
  }

  @PostMapping("/reset-password/otp/validate")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse resetPasswordOtpValidate(@RequestBody @Valid OTPResetPasswordRequest request) {
    log.info("(register)request: {}", request);

    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDateTime.now().toString(),
            "Otp to activate has been sent to the email");
  }
}
