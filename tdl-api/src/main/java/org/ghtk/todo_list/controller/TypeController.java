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
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.model.request.TypeRequest;
import org.ghtk.todo_list.model.response.TypeResponse;
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
@RequestMapping("/api/v1/projects/{project_id}/types")
@RequiredArgsConstructor
@Tag(name = "Type")
public class TypeController {

  private final TypeFacadeService typeFacadeService;
  private final BaseAuthorization baseAuthorization;

  @PostMapping()
  @Operation(description = "Create type")
  public BaseResponse<Type> createType(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @RequestBody @Valid TypeRequest typeRequest) {
    log.info("(createType)projectId: {}, typeRequest: {}", projectId, typeRequest);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        typeFacadeService.createType(getUserId(), projectId, typeRequest.getTitle(), typeRequest.getImage(),
            typeRequest.getDescription()));
  }

  @PutMapping("/{type_id}")
  @Operation(description = "Update type")
  public BaseResponse<Type> updateType(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification type")
      @PathVariable("type_id") String typeId,
      @RequestBody @Valid TypeRequest typeRequest) {
    log.info("(updateType)projectId: {}, typeId: {}, typeRequest: {}", projectId, typeId, typeRequest);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        typeFacadeService.updateType(getUserId(), projectId, typeId, typeRequest.getTitle(), typeRequest.getImage(),
            typeRequest.getDescription()));
  }

  @DeleteMapping("/{type_id}")
  @Operation(description = "Delete type")
  public BaseResponse<String> deleteType(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification type")
      @PathVariable("type_id") String typeId){
    log.info("(deleteType)projectId: {}, typeId: {}", projectId, typeId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    typeFacadeService.deleteType(getUserId(), projectId, typeId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Delete type successfully!");
  }

  @GetMapping()
  @Operation(description = "Get all types")
  public BaseResponse<List<TypeResponse>> getAllTypes(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId){
    log.info("(getAllTypes)projectId: {}", projectId);
    baseAuthorization.allRole(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), typeFacadeService.getAllTypes(getUserId(), projectId));
  }

  @GetMapping("/{type_id}")
  @Operation(description = "Get type")
  public BaseResponse<TypeResponse> getType(
      @Parameter(name = "project_id", description = "Identification project")
      @PathVariable("project_id") String projectId,
      @Parameter(name = "type_id", description = "Identification type")
      @PathVariable("type_id") String typeId){
    log.info("(getType)projectId: {}, typeId: {}", projectId, typeId);
    baseAuthorization.roleAdmin(getUserId(), projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), typeFacadeService.getType(getUserId(), projectId, typeId));
  }
}
