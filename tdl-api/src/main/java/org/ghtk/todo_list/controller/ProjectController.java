package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.model.request.CreateProjectRequest;
import org.ghtk.todo_list.model.request.UpdateProjectRequest;
import org.ghtk.todo_list.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectFacadeService projectService;

  @GetMapping()
  public BaseResponse getAllProject() {
    log.info("(getAllProject)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getAllProject(getUserId()));
  }

  @GetMapping("/{project_id}")
  public BaseResponse getProject(@PathVariable(name = "project_id") String projectId) {
    log.info("(getProject)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getProject(getUserId(), projectId));
  }

  @GetMapping("/{project_id}/information")
  public BaseResponse getProjectInformation(@PathVariable(name = "project_id") String projectId) {
    log.info("(getProjectInformation)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getProjectInformation(getUserId(), projectId));
  }

  @PostMapping()
  public BaseResponse createProject(@RequestBody CreateProjectRequest createProjectRequest) {
    log.info("(createProject)project: {}", createProjectRequest);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        projectService.createProject(getUserId(), createProjectRequest.getTitle()));
  }

  @PutMapping("/{project_id}")
  public BaseResponse updateProject(@PathVariable("project_id") String projectId,
      @RequestBody UpdateProjectRequest updateProjectRequest) {
    log.info("(updateProject)project: {}", updateProjectRequest);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.updateProject(getUserId(), projectId, updateProjectRequest.getTitle(),
            updateProjectRequest.getKeyProject()));
  }
}
