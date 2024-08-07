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
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.model.request.CreateProjectRequest;
import org.ghtk.todo_list.model.request.UpdateProjectRequest;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.ghtk.todo_list.model.response.ProjectRoleResponse;
import org.ghtk.todo_list.paging.PagingReq;
import org.ghtk.todo_list.paging.PagingRes;
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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Project", description = "Project API")
public class ProjectController {

  private final ProjectFacadeService projectService;
  private final BaseAuthorization baseAuthorization;

  @GetMapping()
  @Operation(description = "Get all project for user")
  public BaseResponse<List<ProjectInformationResponse>> getAllProject() {
    log.info("(getAllProject)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getAllProject(getUserId()));
  }

  @GetMapping("/{project_id}")
  @Operation(description = "Get project by id")
  public BaseResponse<ProjectRoleResponse> getProject(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable(name = "project_id") String projectId) {
    log.info("(getProject)projectId: {}", projectId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getProject(getUserId(), projectId));
  }

  @GetMapping("/{project_id}/information")
  @Operation(description = "Get project information by id")
  public BaseResponse<ProjectInformationResponse> getProjectInformation(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable(name = "project_id") String projectId) {
    log.info("(getProjectInformation)projectId: {}", projectId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getProjectInformation(getUserId(), projectId));
  }

  @PostMapping()
  @Operation(description = "Create project")
  public BaseResponse<Project> createProject(
      @RequestBody @Valid CreateProjectRequest createProjectRequest) {
    log.info("(createProject)project: {}", createProjectRequest);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        projectService.createProject(getUserId(), createProjectRequest.getTitle()));
  }

  @PutMapping("/{project_id}")
  @Operation(description = "Update project")
  public BaseResponse<Project> updateProject(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @RequestBody @Valid UpdateProjectRequest updateProjectRequest) {
    log.info("(updateProject)project: {}", updateProjectRequest);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.updateProject(getUserId(), projectId, updateProjectRequest.getTitle(),
            updateProjectRequest.getKeyProject()));
  }

  @DeleteMapping("/{project_id}")
  @Operation(description = "Delete project")
  public BaseResponse<String> deleteProject(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(deleteProject)projectId: {}", projectId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    projectService.deleteProject(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Deleted project successfully!!!");
  }

  @GetMapping("/search")
  @Operation(description = "Search project")
  public BaseResponse<PagingRes<Project>> searchProjects(
      @RequestParam(required = false) String searchValue,
      @Valid PagingReq pageable) {
    log.info("(searchProjects)searchValue: {}, pageable: {}", searchValue, pageable);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.searchProjects(searchValue, pageable.makePageable(), getUserId()));
  }

  @GetMapping("/{project_id}/search-user")
  @Operation(description = "Search user in project")
  public BaseResponse<List<AuthUser>> searchUserInProjects(
      @RequestParam(required = false) String searchValue,
      @RequestParam(required = false) List<String> role,
      @PathVariable("project_id") String projectId) {
    log.info("(searchUserInProjects)searchValue: {}", searchValue);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.searchUser(searchValue, role, projectId, getUserId()));
  }
}
