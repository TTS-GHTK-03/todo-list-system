package org.ghtk.todo_list.controller;

import static org.ghtk.todo_list.util.SecurityUtil.getUserId;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.BaseResponse;
import org.ghtk.todo_list.facade.LabelAttachedFacadeService;
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
public class LabelAttachedController {

  private final LabelAttachedFacadeService service;

  @PostMapping("/types/{type_id}/tasks/{task_id}/labels/{label_id}/attach")
  public BaseResponse create(@PathVariable("project_id") String projectId, @PathVariable("type_id") String typeId,
      @PathVariable("task_id") String taskId, @PathVariable("label_id") String labelId) {
    log.info("(create) Label attached");

    getUserId();
    return BaseResponse.of(HttpStatus.CREATED.value(), LocalDate.now().toString(),
        service.create(projectId, typeId, taskId, labelId));
  }

  @GetMapping("/types/tasks/{task_id}/labels/attach")
  public BaseResponse getLabelAttachedByTask(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId) {
    log.info("(getLabelAttachedByTask)");
    getUserId();
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        service.getLabelAttachedByTask(projectId, taskId));
  }

  @DeleteMapping("/types/tasks/{task_id}/labels/attach/{id}")
  public BaseResponse deleteLabelAttached(@PathVariable("project_id") String projectId,
      @PathVariable("task_id") String taskId, @PathVariable("id") String id) {
    log.info("(deleteLabelAttached)");
    getUserId();
    service.deleteLabelAttached(projectId, taskId, id);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
        "Delete label attached successfully!!");
  }

  @DeleteMapping("/types/tasks/{task_id}/labels/attach")
  public BaseResponse deleteLabelAttachedByTask(@PathVariable("project_id") String projectId,
                                          @PathVariable("task_id") String taskId) {
    log.info("(deleteLabelAttachedByTask)");
    getUserId();
    service.deleteLabelAttachedByTask(projectId, taskId);
    return BaseResponse.of(HttpStatus.OK.value(), LocalDate.now().toString(),
            "Delete label attached by task successfully!!");
  }

}
