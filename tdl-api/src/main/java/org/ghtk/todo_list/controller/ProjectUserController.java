package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.AcceptUserRequest;
import org.ghtk.todo_list.model.request.InviteUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectUserController {

  private final ProjectUserFacadeService projectUserFacadeService;

  @PostMapping("/projects/{project_id}/invite")
  public BaseResponse inviteUser(@PathVariable(name = "project_id") String projectId, @RequestBody @Valid
      InviteUserRequest inviteUserRequest){
    log.info("(inviteUser)projectId: {}", projectId);
    projectUserFacadeService.inviteUser(getUserId(), projectId, inviteUserRequest.getEmail(), inviteUserRequest.getRole());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã gửi lời mời thành công!");
  }

  @GetMapping("/projects/accept")
  public BaseResponse accept(@Valid @RequestParam(value = "emailEncode") String emailEncode){
    log.info("(accept)");
    projectUserFacadeService.accept(emailEncode);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã chấp nhận lời mời!");
  }

  @GetMapping("/users/projects/{project_id}")
  public BaseResponse getAllUserByProject(@PathVariable("project_id") String projectId){
    log.info("(getAllUserByProject)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), projectUserFacadeService.getAllUserByProject(getUserId(), projectId));
  }

  @DeleteMapping("/users/{user_id}/projects/{project_id}")
  public BaseResponse deleteUser(@PathVariable("project_id") String projectId, @PathVariable("user_id") String memberId){
    log.info("(deleteUser)");
    projectUserFacadeService.deleteUser(getUserId(), projectId, memberId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Kick user in project successfully!!");
  }
}
