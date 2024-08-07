package org.ghtk.todo_list.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.mapper.TypeMapper;
import org.ghtk.todo_list.model.response.TypeResponse;
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

  @Override
  public List<TypeResponse> toTypeResponses(List<Type> typeList) {
    List<TypeResponse> typeResponseList = new ArrayList<>();
    for(Type type : typeList){
      TypeResponse typeResponse = new TypeResponse();
      typeResponse.setId(type.getId());
      typeResponse.setTitle(type.getTitle());
      typeResponse.setImage(type.getImage());
      typeResponse.setDescription(type.getDescription());
      typeResponseList.add(typeResponse);
    }
    return typeResponseList;
  }

  @Override
  public TypeResponse toTypeResponse(Type type) {
    TypeResponse typeResponse = new TypeResponse();
    typeResponse.setId(type.getId());
    typeResponse.setTitle(type.getTitle());
    typeResponse.setImage(type.getImage());
    typeResponse.setDescription(type.getDescription());
    return typeResponse;
  }
}
