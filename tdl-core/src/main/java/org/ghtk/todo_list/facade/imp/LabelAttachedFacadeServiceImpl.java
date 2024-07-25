package org.ghtk.todo_list.facade.imp;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.exception.LabelAttachedAlreadyExistsException;
import org.ghtk.todo_list.exception.LabelAttachedNotExistTask;
import org.ghtk.todo_list.exception.LabelAttachedNotFoundException;
import org.ghtk.todo_list.exception.LabelNotFoundException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.exception.TypeNotFoundException;
import org.ghtk.todo_list.facade.LabelAttachedFacadeService;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.ghtk.todo_list.service.LabelService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LabelAttachedFacadeServiceImpl implements LabelAttachedFacadeService {

  private final ProjectService projectService;
  private final LabelAttachedService labelAttachedService;
  private final TaskService taskService;
  private final LabelService labelService;

  @Override
  public LabelAttachedResponse create(String projectId, String typeId, String taskId, String labelId) {
    log.info("(create) projectId: {}, labelId: {}, taskId: {}", projectId, labelId, taskId);

    validateProjectId(projectId);
    validateTypeId(typeId);
    validateTaskId(taskId);
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
  public List<LabelAttachedResponse> getLabelAttachedByTask(String projectId, String taskId) {
    log.info("(getLabelAttachedByType) projectId: {}, taskId: {}", projectId, taskId);
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

  @Override
  public void deleteLabelAttached(String projectId, String taskId, String id) {
    log.info("(deleteLabelAttached)projectId: {}, taskId: {}, id: {}", projectId, taskId, id);
    validateProjectId(projectId);
    validateTaskId(taskId);
    validateLabelAttachedId(id);
    validateProjectIdAndTaskId(projectId, taskId);
    validateTaskIdAndLabelAttachedId(taskId, id);
    labelAttachedService.deleteById(id);
  }

  @Override
  public void deleteLabelAttachedByTask(String projectId, String taskId) {
    log.info("(deleteLabelAttachedByTask)projectId: {}, taskId: {}", projectId, taskId);
    validateProjectIdAndTaskId(projectId, taskId);
    labelAttachedService.deleteAllByTaskId(taskId);
  }

  private void validateProjectId(String projectId){
    log.info("(validateProjectId)projectId: {}", projectId);
    if(!projectService.existById(projectId)){
      log.info("(validateProjectId)projectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

  private void validateProjectIdAndTaskId(String projectId, String taskId) {
    log.info("(validateProjectIdAndTaskId)projectId: {}, taskId: {}", projectId, taskId);
    if (!taskService.existByProjectIdAndTaskId(projectId, taskId)) {
      log.error("(validateProjectIdAndTaskId)taskId: {} not found", taskId);
      throw new TaskNotFoundException();
    }
  }

  private void validateTaskId(String taskId) {
    log.info("(validateTaskId)taskId: {}", taskId);
    if (!taskService.existById(taskId)) {
      log.error("(validateTaskId)taskId: {} not found", taskId);
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

  private void validateTypeId(String typeId) {
    log.info("(validateTypeId)typeId: {}", typeId);
    if (!labelService.existsByTypeId(typeId) || !taskService.existsByTypeId(typeId)) {
      log.error("(validateTypeId)typeId: {} not found", typeId);
      throw new TypeNotFoundException();
    }
  }

  private void validateLabelAttachedId(String labelAttachedId) {
    log.info("(validateLabelAttachedId)labelAttachedId: {}", labelAttachedId);
    if(!labelAttachedService.existsById(labelAttachedId)){
      log.error("(validateLabelAttachedId)labelAttachedId: {} not found", labelAttachedId);
      throw new LabelAttachedNotFoundException();
    }
  }

  private void validateTaskIdAndLabelAttachedId(String taskId, String labelAttachedId){
    log.info("(validateTaskIdAndLabelAttachedId)taskId: {}, labelAttachedId: {}", taskId, labelAttachedId);
    if(!labelAttachedService.existsByTaskIdAndLabelAttachedId(taskId, labelAttachedId)){
      log.error("(validateTaskIdAndLabelAttachedId) labelAttachedId: {} not exist in taskId: {}", labelAttachedId, taskId);
      throw new LabelAttachedNotExistTask();
    }
  }
}
