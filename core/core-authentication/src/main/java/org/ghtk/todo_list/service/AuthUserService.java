package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.AuthUser;

public interface AuthUserService {

  AuthUser findById(String id);
<<<<<<< HEAD
=======
  AuthUser create(String email, String accountId);
>>>>>>> feat: add logic api register
}
