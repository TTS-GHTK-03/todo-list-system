package org.ghtk.todo_list.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.service.impl.TaskServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/projects")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController {

  @Autowired
  private TaskServiceImp taskServiceImp;

  @GetMapping("/{project_id}/tasks")
  public BaseResponse getTasksByProjectId(@PathVariable("project_id") String projectId) {
    List<Task> tasks = taskServiceImp.getAllTasksByProjectId(projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), tasks);
  }
}
