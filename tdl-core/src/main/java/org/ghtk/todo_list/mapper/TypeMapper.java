package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.model.response.TypeResponse;

public interface TypeMapper {
  Type toType(String title, String image, String description);

  List<TypeResponse> toTypeResponses(List<Type> typeList);

  TypeResponse toTypeResponse(Type type);
}
