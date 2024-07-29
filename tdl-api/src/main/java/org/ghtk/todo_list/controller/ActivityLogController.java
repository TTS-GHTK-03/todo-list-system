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
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.ghtk.todo_list.model.response.NotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Activity log", description = "Activity log API")
public class ActivityLogController {

  private final ActivityLogFacadeService activityLogFacadeService;
  private final BaseAuthorization baseAuthorization;

  @GetMapping("/projects/{project_id}/logs")
  @Operation(description = "Get all notifications")
  public BaseResponse<List<NotificationResponse>> getAllNotifications(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable(name = "project_id") String projectId,
      @Valid @RequestParam("page") int page) {
    log.info("(getAllNotifications)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllNotifications(getUserId(), projectId, page));
  }

  @DeleteMapping("/projects/{project_id}/logs/{activity_log_id}")
  @Operation(description = "Delete notification")
  public BaseResponse<String> deleteNotification(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable(name = "project_id") String projectId,
      @Parameter(name = "activity_log_id", description = "Identification of activity log")
      @PathVariable(name = "activity_log_id") String activityLogId) {
    log.info("(deleteNotification)projectId: {}, activityLogId: {}", projectId, activityLogId);
    activityLogFacadeService.deleteNotification(getUserId(), projectId, activityLogId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Deleted notification successfully!!");
  }

  @GetMapping("/projects/{project_id}/tasks/{task_id}/logs")
  @Operation(description = "Get all activity logs by task id")
  public BaseResponse<List<ActivityLog>> getAllActivityLogsByTaskId(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable(name = "project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable(name = "task_id") String taskId) {
    log.info("(getAllActivityLogsByTaskId)projectId: {}, taskId: {}", projectId, taskId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByTaskId(getUserId(), projectId, taskId));
  }

  @GetMapping("/logs")
  @Operation(description = "Get all activity logs by user id")
  public BaseResponse<List<ActivityLog>> getAllActivityLogsByUserId() {
    log.info("(getAllActivityLogsByUserId)userId: {}", getUserId());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByUserId(getUserId()));
  }
}
