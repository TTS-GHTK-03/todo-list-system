package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.model.response.LabelResponse;

public interface LabelFacadeService {

  LabelResponse createLabel(String projectId, String typeId, String title, String description);
  LabelResponse updateLabel(String projectId, String typeId, String labelId, String title, String description);
}
