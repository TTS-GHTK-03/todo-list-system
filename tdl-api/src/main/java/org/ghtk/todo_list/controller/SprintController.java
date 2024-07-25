package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.model.request.SprintRequest;
import org.ghtk.todo_list.model.request.StartSprintRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{project_id}/sprints")
@RequiredArgsConstructor
public class SprintController {

  private final SprintFacadeService sprintFacadeService;

  @PostMapping()
  public BaseResponse createSprintByProject(@PathVariable("project_id") String projectId) {
    log.info("(createSprintByProject) project {}", projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        sprintFacadeService.createSprintByProject(projectId));
  }

  @PutMapping("/{sprint_id}/start")
  public BaseResponse startSprint(@RequestBody @Valid StartSprintRequest request,
      @PathVariable("project_id") String projectId, @PathVariable("sprint_id") String sprintId) {
    log.info("(startSprint) projectId {}, sprintId {}", projectId, sprintId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.startSprint(projectId, sprintId, request.getTitle(),
            request.getStartDate(), request.getEndDate()));
  }

  @PutMapping("/{sprint_id}")
  public BaseResponse updateSprint(@RequestBody @Valid SprintRequest request,
      @PathVariable("project_id") String projectId, @PathVariable("sprint_id") String sprintId) {
    log.info("(updateSprint) projectId {}, sprintId {}", projectId, sprintId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.updateSprint(projectId, sprintId, request.getTitle(),
            request.getStartDate(), request.getEndDate()));
  }

  @GetMapping()
  public BaseResponse getSprints(@PathVariable("project_id") String projectId,
      @RequestParam(value = "status", required = false) String status) {
    log.info("(getSprints) projectId: {}", projectId);
    getUserId();
    if (status == null) {
      log.info("(getSprints) sprints");
      return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
          sprintFacadeService.getSprints(projectId));
    } else {
      log.info("(getSprints) sprints status");
      return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
          sprintFacadeService.getSprintStatus(projectId, status));
    }
  }

  @GetMapping("/{id}")
  public BaseResponse getSprint(@PathVariable("project_id") String projectId,
      @PathVariable("id") String id) {
    log.info("(getSprint) projectId: {}, id: {}", projectId, id);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.getSprint(projectId, id));
  }

  @GetMapping("/{id}/progress")
  public BaseResponse getProgressStatistics(@PathVariable("project_id") String projectId,
      @PathVariable("id") String id) {
    log.info("(getProgressStatistics) projectId: {}, id: {}", projectId, id);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.getProgressStatistics(projectId, id));
  }

  @DeleteMapping("/{id}")
  public BaseResponse deleteSprint(@PathVariable("project_id") String projectId,
      @PathVariable("id") String id) {
    log.info("(deleteSprint) projectId: {}, id: {}", projectId, id);
    getUserId();
    sprintFacadeService.deleteSprint(projectId, id);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete sprint successfully!!");
  }

  @GetMapping("/{sprint_id}/complete")
  public BaseResponse completeSprint(@PathVariable("project_id") String projectId,
      @PathVariable("sprint_id") String sprintId) {
    log.info("(completeSprint) projectId {}, sprintId {}", projectId, sprintId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.completeSprint(projectId, sprintId));
  }
}
