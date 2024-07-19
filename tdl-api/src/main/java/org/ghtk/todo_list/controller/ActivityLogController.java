package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ActivityLogController {

  private final ActivityLogFacadeService activityLogFacadeService;

  @GetMapping("/projects/{project_id}/tasks/{task_id}/log")
  public BaseResponse getAllActivityLogsByTaskId(@PathVariable(name = "project_id") String projectId,
      @PathVariable(name = "task_id") String taskId) {
    log.info("(getAllActivityLogsByTaskId)projectId: {}, taskId: {}", projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByTaskId(getUserId(), projectId, taskId));
  }

  @GetMapping("/log")
  public BaseResponse getAllActivityLogsByUserId() {
    log.info("(getAllActivityLogsByUserId)userId: {}", getUserId());
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogsByUserId(getUserId()));
  }
}
