package org.ghtk.todo_list.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.ActiveAccountRequest;
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
        "Register success and otp to activate has been sent to the email");
  }

  @PostMapping("/active")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse activeAccount(@RequestBody @Valid ActiveAccountRequest request) {
    log.info("(activeAccount)email: {}, otp: {}", request.getEmail(), request.getOtp());
    authFacadeService.activeAccount(request);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        "Active account successfully!!");
  }
}
