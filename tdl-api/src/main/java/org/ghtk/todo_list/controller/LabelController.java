package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.LabelFacadeService;
import org.ghtk.todo_list.model.request.CreateLabelRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/types/{type_id}/labels")
@RequiredArgsConstructor
public class LabelController {

  private final LabelFacadeService labelFacadeService;

  @PostMapping()
  public BaseResponse createLabel(@RequestBody @Valid CreateLabelRequest request,
      @PathVariable("type_id") String typeId) {
    log.info("(createLabel)");

    getUserId();
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        labelFacadeService.createLabel(typeId, request.getTitle(), request.getDescription()));
  }
}
