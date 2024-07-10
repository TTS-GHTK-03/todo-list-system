package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< HEAD
import org.ghtk.todo_list.core_exception.exception.NotFoundException;
import org.ghtk.todo_list.entity.AuthUser;
=======
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.AccountAlreadyHasUserException;
import org.ghtk.todo_list.exception.EmailAlreadyExistedException;
>>>>>>> feat: add logic api register
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.service.AuthUserService;

@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

  private final AuthUserRepository repository;

  @Override
  public AuthUser findById(String id) {
<<<<<<< HEAD
    if (log.isDebugEnabled()) log.debug("(findById)id: {}", id);
=======
    log.info("(findById)id: {}", id);
    if (log.isDebugEnabled())
      log.debug("(findById)id: {}", id);
>>>>>>> feat: add logic api register
    return repository.findById(id).orElseGet(() -> {
      log.error("(findById)id: {} not found", id);
      throw new UserNotFoundException();
    });
  }
<<<<<<< HEAD
=======

  @Override
  public AuthUser create(String email, String accountId) {
    log.info("(create)email: {}", email);

    if (repository.existsByEmail(email)) {
      log.error("(create)email: {} already exist", email);
      throw new EmailAlreadyExistedException(email);
    }
    if (repository.existsByAccountId(accountId)) {
      log.error("(create)userId: {} already has account", accountId);
      throw new AccountAlreadyHasUserException();
    }

    return repository.save(AuthUser.from(email, accountId));
  }
>>>>>>> feat: add logic api register
}
