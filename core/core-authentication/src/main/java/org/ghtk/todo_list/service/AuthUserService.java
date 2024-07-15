package org.ghtk.todo_list.service;

import java.util.List;
import java.util.Optional;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.AuthUser;

public interface AuthUserService {

  AuthUser findById(String id);
  AuthUser create(String email, String accountId);
  boolean existsByEmail(String email);
  Optional<AuthUser> findByAccountId(String accountId);
  List<UserNameResponse> getNameUser(String userId, String projectId);
}
