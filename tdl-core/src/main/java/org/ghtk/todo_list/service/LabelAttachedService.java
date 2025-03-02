package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.LabelAttached;

public interface LabelAttachedService {
  LabelAttached save(LabelAttached labelAttached);
  void deleteByLabelId(String labelId);
  void deleteById(String id);
  boolean existsByLabelIdAndTaskId(String labelId, String taskId);
  List<LabelAttached> getLabelAttachedByTask(String taskId);
  void deleteAllByTaskId(String taskId);
  void deleteAllByLabelId(String labelId);
  boolean existsById(String labelAttachedId);
  boolean existsByTaskIdAndLabelAttachedId(String taskId, String labelAttachedId);
}
