package org.ghtk.todo_list.mapper;

import org.ghtk.todo_list.entity.Type;

public interface TypeMapper {
  Type toType(String title, String image, String description);
}
