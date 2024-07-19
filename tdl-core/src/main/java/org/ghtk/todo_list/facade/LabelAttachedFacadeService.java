package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.LabelAttached;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;

public interface LabelAttachedFacadeService {
  LabelAttachedResponse create(String projectId, String taskId, String labelId);
  List<LabelAttachedResponse> getLabelAttachedByTask(String projectId, String taskId, String labelId);
}
