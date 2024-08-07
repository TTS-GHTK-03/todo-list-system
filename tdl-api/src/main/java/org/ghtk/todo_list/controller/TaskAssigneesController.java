package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.base_authrization.BaseAuthorization;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.entity.TaskAssignees;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Task Assignees")
public class TaskAssigneesController {

  private final TaskFacadeService taskFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping("/users/{user_id}/projects/{project_id}/tasks/{task_id}")
  @Operation(description = "Agile task for user")
  public BaseResponse<TaskAssignees> agileTaskByUser(
      @Parameter(name = "user_id", description = "Identification of invited user")
      @PathVariable("user_id") String userId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "project id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(agileTaskByUser)");
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.agileTaskByUser(userId, taskId));
  }

  @GetMapping("/users/tasks/assignees")
  @Operation(description = "Get all task assignees for user")
  public BaseResponse<List<TaskResponse>> getAllTaskAssigneesForUser() {
    log.info("(getAllTaskAssigneesForUser)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.getAllTaskAssigneesForUser(getUserId()));
  }
}
