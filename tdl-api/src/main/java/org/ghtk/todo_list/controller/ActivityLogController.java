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
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ActivityLogController {

  private final ActivityLogFacadeService activityLogFacadeService;

  @GetMapping("/{project_id}/log")
  public BaseResponse getAllActivityLogs(@PathVariable(name = "project_id") String projectId) {
    log.info("(getAllActivityLogs)");
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        activityLogFacadeService.getAllActivityLogs(getUserId(), projectId));
  }
}
