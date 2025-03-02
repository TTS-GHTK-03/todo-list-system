package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.repository.LabelAttachedRepository;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabelAttachedServiceImpl implements LabelAttachedService {
  private final LabelAttachedRepository repository;

  @Override
  public LabelAttached save(LabelAttached labelAttached) {
    log.info("(save)LabelAttached: {}", labelAttached);
    return repository.save(labelAttached);
  }

  @Override
  public void deleteByLabelId(String labelId) {
    log.info("(deleteByLabelId)labelId: {}", labelId);
    repository.deleteByLabelId(labelId);
  }

  @Override
  public void deleteById(String id) {
    log.info("(deleteById)id: {}", id);
    repository.deleteById(id);
  }

  @Override
  public boolean existsByLabelIdAndTaskId(String labelId, String taskId) {
    log.info("(existsByLabelIdAndTaskId)labelId: {}, taskId: {}", labelId, taskId);
    return repository.existsByLabelIdAndTaskId(labelId, taskId);
  }

  @Override
  public List<LabelAttached> getLabelAttachedByTask(String taskId) {
    log.info("(getLabelAttachedByTask)taskId: {}", taskId);
    return repository.findByTaskId(taskId);
  }

  @Override
  @Transactional
  public void deleteAllByTaskId(String taskId) {
    log.info("(deleteAllByTaskId)taskId: {}", taskId);
    repository.deleteAllByTaskId(taskId);
  }

  @Override
  @Transactional
  public void deleteAllByLabelId(String labelId) {
    log.info("(deleteAllByLabelId)labelId: {}", labelId);
    repository.deleteAllByLabelId(labelId);
  }

  @Override
  public boolean existsById(String labelAttachedId) {
    log.info("(existsById)labelAttachedId: {}", labelAttachedId);
    return repository.existsById(labelAttachedId);
  }

  @Override
  public boolean existsByTaskIdAndLabelAttachedId(String taskId, String labelAttachedId) {
    log.info("(existsByTaskIdAndLabelAttachedId)taskId: {}, labelAttachedId: {})",taskId,labelAttachedId);
    return repository.existsByTaskIdAndId(taskId, labelAttachedId);
  }
}
