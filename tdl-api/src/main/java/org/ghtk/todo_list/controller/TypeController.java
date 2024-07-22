package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.model.request.TypeRequest;
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
public class TypeController {

  private final TypeFacadeService typeFacadeService;

  @PostMapping()
  public BaseResponse createType(@PathVariable("project_id") String projectId,
      @RequestBody @Valid TypeRequest typeRequest) {
    log.info("(createType)projectId: {}, typeRequest: {}", projectId, typeRequest);
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        typeFacadeService.createType(getUserId(), projectId, typeRequest.getTitle(), typeRequest.getImage(),
            typeRequest.getDescription()));
  }

  @PutMapping("/{type_id}")
  public BaseResponse updateType(@PathVariable("project_id") String projectId,
      @PathVariable("type_id") String typeId, @RequestBody @Valid TypeRequest typeRequest) {
    log.info("(updateType)projectId: {}, typeId: {}, typeRequest: {}", projectId, typeId, typeRequest);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        typeFacadeService.updateType(getUserId(), projectId, typeId, typeRequest.getTitle(), typeRequest.getImage(),
            typeRequest.getDescription()));
  }

  @DeleteMapping("/{type_id}")
  public BaseResponse deleteType(@PathVariable("project_id") String projectId, @PathVariable("type_id") String typeId){
    log.info("(deleteType)projectId: {}, typeId: {}", projectId, typeId);
    typeFacadeService.deleteType(getUserId(), projectId, typeId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), "Đã xóa type thành công!");
  }

  @GetMapping()
  public BaseResponse getAllTypes(@PathVariable("project_id") String projectId){
    log.info("(getAllTypes)projectId: {}", projectId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), typeFacadeService.getAllTypes(getUserId(), projectId));
  }

  @GetMapping("/{type_id}")
  public BaseResponse getType(@PathVariable("project_id") String projectId, @PathVariable("type_id") String typeId){
    log.info("(getType)projectId: {}, typeId: {}", projectId, typeId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(), typeFacadeService.getType(getUserId(), projectId, typeId));
  }
}
