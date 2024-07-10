package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.RegisterRequest;

public interface AuthFacadeService {

  void register(RegisterRequest request);
}
