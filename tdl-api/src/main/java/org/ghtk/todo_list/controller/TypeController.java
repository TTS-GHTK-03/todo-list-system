package org.ghtk.todo_list.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.TypeFacadeService;
import org.ghtk.todo_list.model.request.TypeRequest;
import org.springframework.http.HttpStatus;
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
  public BaseResponse createType(@PathVariable("project_id") String projectId, @RequestBody @Valid TypeRequest typeRequest) {
    log.info("(createType)createTypeRequest: {}", typeRequest);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        typeFacadeService.createType(projectId, typeRequest.getTitle(), typeRequest.getImage(),
            typeRequest.getDescription()));
  }
}
