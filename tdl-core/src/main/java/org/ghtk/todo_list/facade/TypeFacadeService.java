package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Type;
import org.ghtk.todo_list.model.response.TypeResponse;

public interface TypeFacadeService {

  Type createType(String userId, String projectId, String title, String image, String description);

  Type updateType(String userId, String projectId, String typeId, String title, String image, String description);

  void deleteType(String userId, String projectId, String typeId);

  List<TypeResponse> getAllTypes(String userId, String projectId);

  TypeResponse getType(String userId, String projectId, String typeId);
}
