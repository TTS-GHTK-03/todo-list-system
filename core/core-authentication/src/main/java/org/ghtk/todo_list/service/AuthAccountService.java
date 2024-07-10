package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.AuthAccount;

public interface AuthAccountService {
  AuthAccount findByUserIdWithThrow(String userId);
<<<<<<< HEAD
=======
  AuthAccount create(String username, String password);

>>>>>>> feat: add logic api register
}
