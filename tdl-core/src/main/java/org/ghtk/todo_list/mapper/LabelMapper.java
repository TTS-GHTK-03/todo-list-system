package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.model.response.LabelResponse;

public interface LabelMapper {

  LabelResponse toLabelResponse(Label label);
  List<LabelResponse> toLabelResponses(List<Label> labels);
}
