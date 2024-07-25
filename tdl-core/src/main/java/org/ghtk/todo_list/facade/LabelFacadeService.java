package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.model.response.LabelAttachedResponse;
import org.ghtk.todo_list.model.response.LabelResponse;

public interface LabelFacadeService {

  LabelResponse createLabel(String projectId, String typeId, String title, String description);
  LabelResponse updateLabel(String projectId, String typeId, String labelId, String title, String description);
  List<LabelResponse> getLabelsByTypeId(String projectId, String typeId);
  List<LabelResponse> getAllLabelByProjectIdAndLabelAttached(String userId, String projectId);
  void deleteLabel(String projectId, String typeId, String labelId);
}
