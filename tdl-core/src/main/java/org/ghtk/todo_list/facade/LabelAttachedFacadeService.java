package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;

public interface LabelAttachedFacadeService {
  LabelAttachedResponse create(String projectId, String typeId, String taskId, String labelId);
  List<LabelAttachedResponse> getLabelAttachedByTask(String projectId, String taskId);
  void deleteLabelAttached(String projectId, String taskId, String id);
  void deleteLabelAttachedByTask(String projectId, String taskId);
}
