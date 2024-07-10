package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.entity.AuthUser;

public interface AuthUserService {

  AuthUser findById(String id);

  AuthUser findByEmailWithThrow(String email);
}
