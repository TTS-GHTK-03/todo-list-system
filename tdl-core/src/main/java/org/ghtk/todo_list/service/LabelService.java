package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Label;

public interface LabelService {

  Label save(Label label);

  boolean existByTypeIdAndTitle(String typeId, String title);

  Label findById(String id);

  List<Label> getLabelsByType(String typeId);
  void deleteLabel(String id);
  void deleteByTypeId(String typeId);
  boolean existsById(String id);
  void deleteAllByTypeId(String typeId);
  boolean existsByTypeId(String typeId);
  boolean existsByTypeIdAndLabelId(String typeId, String labelId);
}
