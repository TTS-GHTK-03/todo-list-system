package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.AcceptUserRequest;
import org.ghtk.todo_list.model.request.InviteUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectUserController {

  private final ProjectUserFacadeService projectUserFacadeService;

  @PostMapping("{project_id}/invite")
  public BaseResponse inviteUser(@PathVariable(name = "project_id") String projectId, @RequestBody
      InviteUserRequest inviteUserRequest){
    log.info("(inviteUser)projectId: {}", projectId);
    projectUserFacadeService.inviteUser(getUserId(), projectId, inviteUserRequest.getEmail(), inviteUserRequest.getRole());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã gửi lời mời thành công!");
  }

  @PostMapping("/accept")
  public BaseResponse accept(@RequestBody AcceptUserRequest acceptUserRequest){
    log.info("(accept)");
    projectUserFacadeService.accept(acceptUserRequest.getEmail());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã chấp nhận lời mời!");
  }
}
