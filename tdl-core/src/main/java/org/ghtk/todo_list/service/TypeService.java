package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Type;

public interface TypeService {

  boolean existById(String id);
  Type findById(String id);
}
