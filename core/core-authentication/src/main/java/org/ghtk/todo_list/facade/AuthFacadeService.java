package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.dto.request.ActiveAccountRequest;
import org.ghtk.todo_list.dto.request.RegisterRequest;
import org.ghtk.todo_list.entity.AuthAccount;

public interface AuthFacadeService {

  void register(RegisterRequest request);
  void activeAccount(ActiveAccountRequest request);

}
