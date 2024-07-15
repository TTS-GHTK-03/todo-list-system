package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.model.request.StartSprintRequest;
import org.ghtk.todo_list.service.SprintService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PutMapping("/{project_id}/start/{sprint_id}")
  public BaseResponse startSprint(@RequestBody @Valid StartSprintRequest request,  @PathVariable("project_id") String projectId, @PathVariable("sprint_id") String sprintId) {
    log.info("(startSprint) projectId {}, sprintId {}", projectId, sprintId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintService.startSprint(projectId, sprintId, request.getTitle(), request.getStartDate(), request.getEndDate()));
  }
}
