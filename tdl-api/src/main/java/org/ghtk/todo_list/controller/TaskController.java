package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.request.UpdateDueDateTaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {

  @Autowired
  private TaskFacadeService taskFacadeService;

  @GetMapping("/{project_id}/tasks")
  public BaseResponse getTasksByProjectId(@PathVariable("project_id") String projectId) {
    log.info("(getAllTasks)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getAllTaskByProjectId(getUserId(), projectId));
  }

  @GetMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse getTaskByTaskId(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(getTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getTaskByTaskId(getUserId(), projectId, taskId));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse updateStatusTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @Valid @RequestParam(value = "statusTask") String status) {
    log.info("(updateStatusTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStatusTask(getUserId(), projectId, taskId, status));
  }

  @PatchMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}")
  public BaseResponse updateSprintTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @PathVariable("sprint_id") String sprintId) {
    log.info("(updateSprintTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateSprintTask(getUserId(), projectId, sprintId, taskId));
  }

  @PostMapping("/{project_id}/tasks/{task_id}/clone")
  public BaseResponse cloneTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(cloneTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.cloneTask(getUserId(), projectId, taskId));
  }

  @PutMapping("/{project_id}/sprints/{sprint_id}/tasks/{task_id}/update-date")
  public BaseResponse updateStartDateDueDateTask(@PathVariable("project_id") String projectId,
      @PathVariable("sprint_id") String sprintId, @PathVariable("task_id") String taskId,
      @RequestBody @Valid UpdateDueDateTaskRequest updateDueDateTaskRequest) {
    log.info("(updateStartDateDueDateTask)project: {}, sprint: {}, task: {}", projectId, sprintId,
        taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.updateStartDateDueDateTask(getUserId(), projectId, sprintId, taskId,
            updateDueDateTaskRequest.getStatusTaskKey(),
            updateDueDateTaskRequest.getDueDate()));
  }
}
