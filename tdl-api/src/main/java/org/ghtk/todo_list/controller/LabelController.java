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
import org.ghtk.todo_list.facade.LabelFacadeService;
import org.ghtk.todo_list.model.request.CreateLabelRequest;
import org.ghtk.todo_list.model.request.UpdateLabelRequest;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects/{project_id}")
@RequiredArgsConstructor
@Tag(name = "Label", description = "Label API")
public class LabelController {

  private final LabelFacadeService labelFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping("/types/{type_id}/labels")
  @Operation(description = "Create label")
  public BaseResponse<LabelResponse> createLabel(@RequestBody @Valid CreateLabelRequest request,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification of type")
      @PathVariable("type_id") String typeId) {
    log.info("(createLabel)");
    baseAuthorization.roleAdmin(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        labelFacadeService.createLabel(projectId, typeId, request.getTitle(),
            request.getDescription()));
  }

  @PutMapping("/types/{type_id}/labels/{id}")
  @Operation(description = "Update label")
  public BaseResponse<LabelResponse> updateLabel(@RequestBody @Valid UpdateLabelRequest request,
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification of type")
      @PathVariable("type_id") String typeId,
      @Parameter(name = "id", description = "Identification of label")
      @PathVariable("id") String labelId) {
    log.info("(updateLabel)");
    baseAuthorization.roleAdmin(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        labelFacadeService.updateLabel(projectId, typeId, labelId, request.getTitle(),
            request.getDescription()));
  }

  @GetMapping("/types/{type_id}/labels")
  @Operation(description = "Get all labels by type id")
  public BaseResponse<List<LabelResponse>> getLabelsByTypeId(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification of type")
      @PathVariable("type_id") String typeId) {
    log.info("(getLabelsByTypeId)");
    baseAuthorization.roleAdminAndEdit(getUserId(), projectId);
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        labelFacadeService.getLabelsByTypeId(projectId, typeId));
  }

  @GetMapping("/labels/attached")
  @Operation(description = "Get all label by project id and label attached")
  public BaseResponse<List<LabelResponse>> getAllLabelByProjectIdAndLabelAttached(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId) {
    log.info("(getAllLabelByProjectIdAndLabelAttached)projectId: {}", projectId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        labelFacadeService.getAllLabelByProjectIdAndLabelAttached(getUserId(), projectId));
  }

  @DeleteMapping("/types/{type_id}/labels/{id}")
  @Operation(description = "Delete label")
  public BaseResponse<String> deleteLabel(
      @Parameter(name = "project_id", description = "Identification of project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification of type")
      @PathVariable("type_id") String typeId,
      @Parameter(name = "id", description = "Identification of label")
      @PathVariable("id") String labelId) {
    log.info("(deleteLabel)");
    baseAuthorization.roleAdmin(getUserId(), projectId);
    getUserId();
    labelFacadeService.deleteLabel(projectId, typeId, labelId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete label successfully!!");
  }

}
