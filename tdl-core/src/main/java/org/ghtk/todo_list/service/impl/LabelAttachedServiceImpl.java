package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.repository.LabelAttachedRepository;
import org.ghtk.todo_list.service.LabelAttachedService;
import org.springframework.stereotype.Service;

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
}
