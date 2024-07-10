package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.AuthAccount;

public interface AuthAccountService {
  AuthAccount findByUserIdWithThrow(String userId);
  AuthAccount create(String username, String password);
}
