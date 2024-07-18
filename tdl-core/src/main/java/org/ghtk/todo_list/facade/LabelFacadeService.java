package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.model.response.LabelResponse;

public interface LabelFacadeService {

  LabelResponse createLabel(String typeId, String title, String description);
}
