package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.model.request.CreateProjectRequest;
import org.ghtk.todo_list.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping()
  public BaseResponse getAllProject() {
    log.info("(getAllProject)");
    String userId = "58ba2b2c-fa18-4443-bcb5-ea24ad60b319";
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getAllProject(userId));
  }

  @GetMapping("/{project_id}")
  public BaseResponse getProject(@PathVariable(name = "project_id") String projectId) {
    log.info("(getProject)projectId: {}", projectId);
    String userId = "58ba2b2c-fa18-4443-bcb5-ea24ad60b319";
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        projectService.getProject(userId, projectId));
  }

  @PostMapping()
  public BaseResponse createProject(@RequestBody CreateProjectRequest createProjectRequest) {
    log.info("(createProject)project: {}", createProjectRequest);
    String userId = "58ba2b2c-fa18-4443-bcb5-ea24ad60b319";
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        projectService.createProject(userId, createProjectRequest.getTitle(),
            createProjectRequest.getRole()));
  }
}
