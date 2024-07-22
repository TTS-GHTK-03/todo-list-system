package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.Type;

public interface TypeService {

  Type createType(Type type);

  Type updateType(Type type);
  boolean existById(String id);

  boolean existByProjectIdAndTitle(String projectId, String title);

  Type findById(String id);
  List<Type> findAllByProjectId(String projectId);

  void deleteAllByProjectId(String projectId);
}
