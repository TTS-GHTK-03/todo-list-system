package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TDLFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        tdlFacadeService.getAllTaskByProjectId(getUserId(), projectId));
  }

  @GetMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse getTaskByTaskId(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(getTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        tdlFacadeService.getTaskByTaskId(getUserId(), projectId, taskId));
  }

  @PatchMapping("/{project_id}/tasks/{task_id}")
  public BaseResponse updateStatusTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @Valid @RequestParam(value = "statusTask") String status) {
    log.info("(updateStatusTask)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        tdlFacadeService.updateStatusTask(getUserId(), projectId, taskId, status));
  }
}
