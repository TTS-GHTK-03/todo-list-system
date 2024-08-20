package org.ghtk.todo_list.service;

import java.util.Optional;
import org.ghtk.todo_list.entity.AuthAccount;

public interface AuthAccountService {

  AuthAccount findByUserIdWithThrow(String userId);
  AuthAccount create(String username, String password);
  Optional<AuthAccount> findByEmail(String email);
  AuthAccount save(AuthAccount authAccount);
  Optional<AuthAccount> findByUsername(String username);
  void updateLockPermanentById(String id, boolean isLockPermanent);
  String findUsernameByUserId(String userId);
}
