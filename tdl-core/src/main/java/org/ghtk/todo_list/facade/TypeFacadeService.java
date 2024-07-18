package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.entity.Type;

public interface TypeFacadeService {

  Type createType(String userId, String projectId, String title, String image, String description);

  Type updateType(String userId, String projectId, String typeId, String title, String image, String description);
}
