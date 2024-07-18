package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Label;

public interface LabelService {

  Label save(Label label);

  boolean existByTypeIdAndTitle(String typeId, String title);
}
