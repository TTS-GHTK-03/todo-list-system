package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TaskFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TaskAssigneesController {

  @Autowired
  private TaskFacadeService taskFacadeService;

  @PostMapping("/users/{user_id}/tasks/{task_id}")
  public BaseResponse agileTaskByUser(@PathVariable("user_id") String userId, @PathVariable("task_id") String taskId) {
    log.info("(agileTaskByUser)");
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        taskFacadeService.agileTaskByUser(userId, taskId));
  }
}
