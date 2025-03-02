package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.base_authrization.BaseAuthorization;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ProjectUserFacadeService;
import org.ghtk.todo_list.model.request.InviteUserRequest;
import org.ghtk.todo_list.model.request.ShareUserRequest;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.model.request.UpdateRoleProjectUserRequest;
import org.ghtk.todo_list.model.response.AcceptShareResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "project-user")
public class ProjectUserController {

  private final ProjectUserFacadeService projectUserFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping("/projects/{project_id}/invite")
  @Operation(description = "Invite user")
  public BaseResponse<String> inviteUser(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable(name = "project_id") String projectId,
      @Parameter(name = "reSend", description = "Resend invitation")
      @RequestParam(value = "reSend", required = false) Boolean reSend,
      @RequestBody @Valid InviteUserRequest inviteUserRequest) {
    log.info("(inviteUser)projectId: {}", projectId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    projectUserFacadeService.inviteUser(getUserId(), projectId, inviteUserRequest.getEmail(),
        inviteUserRequest.getRole(), reSend);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Invitation sent successfully!");
  }

  @PostMapping("/accept")
  @Operation(description = "Accept invitation")
  public BaseResponse<?> accept(
      @Parameter(name = "emailEncode", description = "Email encode")
      @Valid @RequestParam(value = "emailEncode") String emailEncode,
      @Parameter(name = "projectId", description = "Identification project")
      @Valid @RequestParam(value = "projectId") String projectId) {
    log.info("(accept)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectUserFacadeService.accept(getUserId(), emailEncode, projectId));
  }

  @PostMapping("/projects/{project_id}/share")
  @Operation(description = "Share project")
  public BaseResponse<String> shareProject(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable(name = "project_id") String projectId,
      @RequestBody @Valid ShareUserRequest shareUserRequest) {
    log.info("(shareProject)projectId: {}", projectId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    projectUserFacadeService.shareProject(getUserId(), projectId, shareUserRequest.getEmail(),
        shareUserRequest.getRole(), shareUserRequest.getExpireTime());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Share project successfully!");
  }

  @PostMapping("/view_share")
  @Operation(description = "View share project")
  public BaseResponse<AcceptShareResponse> viewShareProject(
      @Parameter(name = "shareToken", description = "Share token")
      @Valid @RequestParam(name = "shareToken") String shareToken) {
    log.info("(viewShareProject)shareToken: {}", shareToken);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectUserFacadeService.viewShareProject(getUserId(), shareToken));
  }

  @GetMapping("/users/projects/{project_id}")
  @Operation(description = "Get all user in project")
  public BaseResponse<List<UserResponse>> getAllUserByProject(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId) {
    log.info("(getAllUserByProject)projectId: {}", projectId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectUserFacadeService.getAllUserByProject(getUserId(), projectId));
  }

  @PutMapping("/projects/{project_id}/role")
  @Operation(description = "Update role user in project")
  public BaseResponse<String> updateRoleProjectUser(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @RequestBody @Valid UpdateRoleProjectUserRequest updateRoleProjectUserRequest) {
    log.info("(updateRoleProjectUser)projectId: {}", projectId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectUserFacadeService.updateRoleProjectUser(projectId,
            updateRoleProjectUserRequest.getMemberId(), updateRoleProjectUserRequest.getRole()));
  }

  @DeleteMapping("/users/{user_id}/projects/{project_id}")
  @Operation(description = "Kick user in project")
  public BaseResponse<String> deleteUser(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "user_id", description = "Identification user")
      @PathVariable("user_id") String memberId) {
    log.info("(deleteUser)");
    baseAuthorization.roleAdmin(getUserId(), projectId);
    projectUserFacadeService.deleteUser(getUserId(), projectId, memberId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Kick user in project successfully!!");
  }
}
