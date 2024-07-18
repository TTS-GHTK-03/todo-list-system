package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Type;

public interface TypeService {

  Type createType(Type type);

  boolean existById(String id);

  boolean existByProjectIdAndTitle(String projectId, String title);

  Type findById(String id);
}
