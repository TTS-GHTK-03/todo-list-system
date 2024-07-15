package org.ghtk.todo_list.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.service.SprintService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/sprints")
@RequiredArgsConstructor
public class SprintController {

  private final SprintService sprintService;

  @PostMapping("/{project_id}")
  public BaseResponse createSprintByProject(@PathVariable("project_id") String projectId) {
    log.info("(createSprintByProject) project {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintService.createSprintByProject(projectId));
  }

  @PostMapping("/{project_id}/start/{sprint_id}")
  public BaseResponse startSprint(@PathVariable("project_id") String projectId, @PathVariable("sprint_id") String sprintId) {
    log.info("(startSprint) projectId {}, sprintId {}", projectId, sprintId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintService.createSprintByProject(projectId));
  }
}
