package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
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

public class ActivityLogController {

  private final ActivityLogFacadeService activityLogFacadeService;

  @GetMapping("/projects/{project_id}/logs")
  public BaseResponse getAllNotifications(
      @PathVariable(name = "project_id") String projectId, @Valid @RequestParam("page") int page) {
    log.info("(getAllNotifications)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllNotifications(getUserId(), projectId, page));
  }

  @DeleteMapping("/projects/{project_id}/logs/{activity_log_id}")
  public BaseResponse deleteNotification(@PathVariable(name = "project_id") String projectId, @PathVariable(name = "activity_log_id") String activityLogId) {
    log.info("(deleteNotification)projectId: {}, activityLogId: {}", projectId, activityLogId);
    activityLogFacadeService.deleteNotification(getUserId(), projectId, activityLogId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã xóa thành công thông báo!");
  }

  @GetMapping("/projects/{project_id}/tasks/{task_id}/logs")
  public BaseResponse getAllActivityLogsByTaskId(@PathVariable(name = "project_id") String projectId,
      @PathVariable(name = "task_id") String taskId) {
    log.info("(getAllActivityLogsByTaskId)projectId: {}, taskId: {}", projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByTaskId(getUserId(), projectId, taskId));
  }

  @GetMapping("/logs")
  public BaseResponse getAllActivityLogsByUserId() {
    log.info("(getAllActivityLogsByUserId)userId: {}", getUserId());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByUserId(getUserId()));
  }
}
