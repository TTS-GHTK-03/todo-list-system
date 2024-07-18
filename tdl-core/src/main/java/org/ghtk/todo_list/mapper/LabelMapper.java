package org.ghtk.todo_list.mapper;

import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.model.response.LabelResponse;

public interface LabelMapper {

  LabelResponse toLabelResponse(Label label);
}
