package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.LabelAttached;

public interface LabelAttachedService {
  LabelAttached save(LabelAttached labelAttached);
  void deleteByLabelId(String labelId);
  void deleteById(String id);
  boolean existsByLabelIdAndTaskId(String labelId, String taskId);
}
