package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.entity.Type;

public interface TypeFacadeService {

  Type createType(String projectId, String title, String image, String description);
}
