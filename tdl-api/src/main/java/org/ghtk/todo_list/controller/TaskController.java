package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.facade.TDLFacadeService;
import org.ghtk.todo_list.model.request.CreateProjectRequest;
import org.ghtk.todo_list.model.request.CreateTaskRequest;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.service.TaskService;
import org.ghtk.todo_list.service.impl.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {

  @Autowired
  private TDLFacadeService tdlFacadeService;

  @GetMapping("/{project_id}/tasks")
  public BaseResponse getTasksByProjectId(@PathVariable("project_id") String projectId) {
    log.info("(getAllTasks)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        tdlFacadeService.getAllTaskByProjectId(projectId));
  }

  @GetMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse getTaskByTaskId(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(getTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        tdlFacadeService.getTaskByTaskId(projectId, taskId));
  }

  @PostMapping("/{project_id}/tasks")
  public BaseResponse createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest,
      @PathVariable("project_id") String projectId) {
    log.info("(createTask)task: {}", createTaskRequest);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        tdlFacadeService.createTask(projectId, getUserId(),
            createTaskRequest.getTitle()));
  }

}
