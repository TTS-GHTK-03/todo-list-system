package org.ghtk.todo_list.mapper.impl;

import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.springframework.stereotype.Component;

@Component
public class TypeMapperImpl implements TypeMapper {

  @Override
  public Type toType(String title, String image, String description) {
    Type type = new Type();
    type.setTitle(title);
    type.setImage(image);
    type.setDescription(description);
    return type;
  }
}
