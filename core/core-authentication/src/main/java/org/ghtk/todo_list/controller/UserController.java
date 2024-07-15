package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.ChangePasswordRequest;
import org.ghtk.todo_list.dto.request.UpdateInformationRequest;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.AuthFacadeService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.util.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/users")
@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserController {

  private final AuthFacadeService authFacadeService;
  private final AuthUserService authUserService;

  @PutMapping("/change-password")
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse changePassword(@RequestBody @Valid ChangePasswordRequest request) {

    log.info("(changePassword)request: {}", request.toString());
    authFacadeService.changePassword(request, "");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        "Change Password successfully!!");
  }

  @PatchMapping
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse updateInformation(@RequestBody @Valid UpdateInformationRequest request) {
    log.info("(updateInformation)request: {}", request.toString());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authUserService.updateUserDetail(getUserId(), request));
  }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public BaseResponse getDetail() {
    log.info("(getDetail)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDateTime.now().toString(),
        authUserService.getDetail(getUserId()));
  }
}
