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
import org.ghtk.todo_list.facade.SprintFacadeService;
import org.ghtk.todo_list.model.request.SprintRequest;
import org.ghtk.todo_list.model.request.StartSprintRequest;
import org.ghtk.todo_list.model.response.CompleteSprintResponse;
import org.ghtk.todo_list.model.response.CreateSprintResponse;
import org.ghtk.todo_list.model.response.ProgressStatisticsResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.model.response.StartSprintResponse;
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
@Tag(name = "sprint")
public class SprintController {

  private final SprintFacadeService sprintFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping()
  @Operation(description = "Create sprint by project")
  public BaseResponse<CreateSprintResponse> createSprintByProject(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId) {
    log.info("(createSprintByProject) project {}", projectId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        sprintFacadeService.createSprintByProject(getUserId(), projectId));
  }

  @PutMapping("/{sprint_id}/start")
  @Operation(description = "Start sprint")
  public BaseResponse<StartSprintResponse> startSprint(
      @RequestBody @Valid StartSprintRequest request,
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(startSprint) projectId {}, sprintId {}", projectId, sprintId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.startSprint(getUserId(), projectId, sprintId, request.getTitle(),
            request.getStartDate(), request.getEndDate()));
  }

  @PutMapping("/{sprint_id}")
  @Operation(description = "Update sprint")
  public BaseResponse<SprintResponse> updateSprint(
      @RequestBody @Valid SprintRequest request,
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(updateSprint) projectId {}, sprintId {}", projectId, sprintId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.updateSprint(projectId, sprintId, request.getTitle(),
            request.getStartDate(), request.getEndDate()));
  }

  @GetMapping()
  @Operation(description = "Get sprints or search sprints by status")
  public BaseResponse<List<SprintResponse>> getSprints(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "status", description = "Status sprint", example = "IN_PROGRESS")
      @RequestParam(value = "status", required = false) String status) {
    log.info("(getSprints) projectId: {}", projectId);
    baseAuthorization.allRole(getUserId(), projectId);
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
  @Operation(description = "Get sprint by id")
  public BaseResponse<SprintResponse> getSprint(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("id") String id) {
    log.info("(getSprint) projectId: {}, id: {}", projectId, id);
    baseAuthorization.allRole(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.getSprint(projectId, id));
  }

  @GetMapping("/{id}/progress")
  @Operation(description = "Get progress statistics")
  public BaseResponse<ProgressStatisticsResponse> getProgressStatistics(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("id") String id) {
    log.info("(getProgressStatistics) projectId: {}, id: {}", projectId, id);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.getProgressStatistics(projectId, id));
  }

  @DeleteMapping("/{id}")
  @Operation(description = "Delete sprint by id")
  public BaseResponse<String> deleteSprint(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("id") String id) {
    log.info("(deleteSprint) projectId: {}, id: {}", projectId, id);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    sprintFacadeService.deleteSprint(getUserId(), projectId, id);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete sprint successfully!!");
  }

  @GetMapping("/{sprint_id}/complete")
  @Operation(description = "Complete sprint")
  public BaseResponse<CompleteSprintResponse> completeSprint(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(completeSprint) projectId {}, sprintId {}", projectId, sprintId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        sprintFacadeService.completeSprint(projectId, sprintId));
  }

  @PutMapping("/{sprint_id}/confirm-complete")
  @Operation(description = "Confirm complete sprint")
  public BaseResponse<String> confirmCompleteSprint(
      @Parameter(name = "project id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "sprint id", description = "Identification sprint")
      @PathVariable("sprint_id") String sprintId) {
    log.info("(confirmCompleteSprint) projectId {}, sprintId {}", projectId, sprintId);
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    sprintFacadeService.confirmCompleteSprint(getUserId(), projectId, sprintId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Complete sprint successfully!!");
  }
}
