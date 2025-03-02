package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.base_authrization.BaseAuthorization;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.LabelAttachedFacadeService;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/projects/{project_id}")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Label attached", description = "Label attached API")
public class LabelAttachedController {

  private final LabelAttachedFacadeService service;
  private final BaseAuthorization baseAuthorization;

  @PostMapping("/types/{type_id}/tasks/{task_id}/labels/{label_id}/attach")
  @Operation(description = "Create label attached")
  public BaseResponse<LabelAttachedResponse> create(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification of type")
      @PathVariable("type_id") String typeId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "label_id", description = "Identification of label")
      @PathVariable("label_id") String labelId) {
    log.info("(create) Label attached");
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        service.create(projectId, typeId, taskId, labelId));
  }

  @GetMapping("/types/tasks/{task_id}/labels/attach")
  @Operation(description = "Get all label attached by task")
  public BaseResponse<List<LabelAttachedResponse>> getLabelAttachedByTask(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId) {
    log.info("(getLabelAttachedByTask)");
    baseAuthorization.allRole(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        service.getLabelAttachedByTask(projectId, taskId));
  }

  @DeleteMapping("/types/tasks/{task_id}/labels/attach/{id}")
  @Operation(description = "Delete label attached")
  public BaseResponse<String> deleteLabelAttached(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId,
      @Parameter(name = "id", description = "Identification of label attached")
      @PathVariable("id") String id) {
    log.info("(deleteLabelAttached)");
    baseAuthorization.roleAdmin(getUserId(), projectId);
    getUserId();
    service.deleteLabelAttached(projectId, taskId, id);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete label attached successfully!!");
  }

  @DeleteMapping("/types/tasks/{task_id}/labels/attach")
  @Operation(description = "Delete label attached by task")
  public BaseResponse<String> deleteLabelAttachedByTask(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "task_id", description = "Identification of task")
      @PathVariable("task_id") String taskId) {
    log.info("(deleteLabelAttachedByTask)");
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    service.deleteLabelAttachedByTask(projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
            "Delete label attached by task successfully!!");
  }

}
