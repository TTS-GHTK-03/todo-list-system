package org.ghtk.todo_list.facade.imp;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.exception.LabelAttachedAlreadyExistsException;
import org.ghtk.todo_list.exception.LabelNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.facade.LabelAttachedFacadeService;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LabelAttachedFacadeServiceImpl implements LabelAttachedFacadeService {

  private final LabelAttachedService labelAttachedService;
  private final TaskService taskService;
  private final LabelService labelService;

  @Override
  public LabelAttachedResponse create(String projectId, String taskId, String labelId) {
    log.info("(create) projectId: {}, labelId: {}, taskId: {}", projectId, labelId, taskId);
    validateLabelId(labelId);
    validateProjectIdAndTaskId(projectId, taskId);
    if(labelAttachedService.existsByLabelIdAndTaskId(labelId, taskId)) {
      log.error("(create)Label attached already exists labelId: {}, taskId: {}", labelId, taskId);
      throw new LabelAttachedAlreadyExistsException();
    }

    LabelAttached labelAttached = new LabelAttached();
    labelAttached.setLabelId(labelId);
    labelAttached.setTaskId(taskId);
    labelAttached = labelAttachedService.save(labelAttached);
    log.info("(create) labelAttached: {}", labelAttached);
    return LabelAttachedResponse.builder()
        .id(labelAttached.getId())
        .labelId(labelAttached.getLabelId())
        .taskId(labelAttached.getTaskId())
        .build();
  }

  @Override
  public List<LabelAttachedResponse> getLabelAttachedByTask(String projectId, String taskId,
      String labelId) {
    log.info("(getLabelAttachedByType) projectId: {}, labelId: {}, taskId: {}", projectId, labelId, taskId);
    validateLabelId(labelId);
    validateProjectIdAndTaskId(projectId, taskId);
    List<LabelAttached> labelAttachedList = labelAttachedService.getLabelAttachedByTask(taskId);
    log.info("(getLabelAttachedByType) LabelAttached: {}", labelAttachedList);
    return labelAttachedList.stream().map(labelAttached -> {
      return LabelAttachedResponse.builder()
          .id(labelAttached.getId())
          .taskId(labelAttached.getTaskId())
          .labelId(labelAttached.getLabelId())
          .build();
    }).collect(Collectors.toList());
  }

  private void validateProjectIdAndTaskId(String projectId, String taskId) {
    log.info("(validateProjectIdAndTaskId)projectId: {}, taskId: {}", projectId, taskId);
    if (!taskService.existByProjectIdAndTaskId(projectId, taskId)) {
      log.error("(validateProjectIdAndTaskId)taskId: {} not found", taskId);
      throw new TaskNotFoundException();
    }
  }

  private void validateLabelId(String labelId) {
    log.info("(validateLabelId)labelId: {}", labelId);
    if (!labelService.existsById(labelId)) {
      log.error("(validateLabelId)labelId: {} not found", labelId);
      throw new LabelNotFoundException();
    }
  }
}
