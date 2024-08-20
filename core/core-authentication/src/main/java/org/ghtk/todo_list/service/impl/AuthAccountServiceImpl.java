package org.ghtk.todo_list.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.AuthAccount;
import org.ghtk.todo_list.exception.AccountNotFoundException;
import org.ghtk.todo_list.exception.UsernameAlreadyExistedException;
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

  @Override
  public AuthAccount create(String username, String password) {
    log.info("(create)username: {}", username);

    if (repository.existsByUsername(username)) {
      log.error("(create)username: {} already exists", username);
      throw new UsernameAlreadyExistedException(username);
    }

    return repository.save(AuthAccount.of(username, password));
  }

  @Override
  public Optional<AuthAccount> findByEmail(String email) {
    log.info("(findByEmail)email: {}", email);
    return repository.findByEmail(email);
  }

  @Override
  public AuthAccount save(AuthAccount authAccount) {
    log.info("(save)authAccount: {}", authAccount);
    return repository.save(authAccount);
  }

  @Override
  public Optional<AuthAccount> findByUsername(String username) {
    log.info("(findByUsername)username: {}", username);
    return repository.findByUsername(username);
  }

  @Override
  public void updateLockPermanentById(String id, boolean isLockPermanent) {
    log.info("(updateLockPermanentById)id: {}, isLockPermanent: {}", id, isLockPermanent);
    repository.updateLockPermanentById(id, isLockPermanent);
  }

  @Override
  public String findUsernameByUserId(String userId) {
    log.info("(findUsernameByUserId)userId: {}", userId);
    return repository.findUsernameByUserId(userId);
  }
}
