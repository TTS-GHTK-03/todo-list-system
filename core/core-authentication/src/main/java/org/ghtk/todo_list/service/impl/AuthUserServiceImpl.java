package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.service.AuthUserService;

@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

  private final AuthUserRepository repository;

  @Override
  public AuthUser findById(String id) {
    if (log.isDebugEnabled()) log.debug("(findById)id: {}", id);
    return repository.findById(id).orElseGet(() -> {
      log.error("(findById)id: {} not found", id);
      throw new UserNotFoundException();
    });
  }

  @Override
  public AuthUser findByEmailWithThrow(String email) {
    if (log.isDebugEnabled()) log.debug("(findByEmail)email: {}", email);
    return repository.findByEmail(email).orElseGet(() -> {
      log.error("(findByEmail)id: {} not found", email);
      throw new UserNotFoundException(); // EmailNotFound
    });
  }
}
