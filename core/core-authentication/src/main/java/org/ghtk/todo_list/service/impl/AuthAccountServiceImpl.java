package org.ghtk.todo_list.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.exception.AccountNotFoundException;
<<<<<<< HEAD
=======
import org.ghtk.todo_list.exception.UsernameAlreadyExistedException;
>>>>>>> feat: add logic api register
import org.ghtk.todo_list.repository.AuthAccountRepository;
import org.ghtk.todo_list.service.AuthAccountService;

@Slf4j
@RequiredArgsConstructor
public class AuthAccountServiceImpl implements AuthAccountService {

  private final AuthAccountRepository repository;

  @Override
  public AuthAccount findByUserIdWithThrow(String userId) {
    log.debug("(findByUserIdWithThrow)userId: {}", userId);
    return repository.findFirstByUserId(userId)
        .orElseThrow(() -> {
          log.error("(findByUserIdWithThrow)userId: {} not found", userId);
          throw new AccountNotFoundException();
        });
  }
<<<<<<< HEAD
=======

  @Override
  public AuthAccount create(String username, String password) {
    log.info("(create)username: {}", username);

    if (repository.existsByUsername(username)) {
      log.error("(create)username: {} already exists", username);
      throw new UsernameAlreadyExistedException(username);
    }

    return repository.save(AuthAccount.of(username, password));
  }
>>>>>>> feat: add logic api register
}
