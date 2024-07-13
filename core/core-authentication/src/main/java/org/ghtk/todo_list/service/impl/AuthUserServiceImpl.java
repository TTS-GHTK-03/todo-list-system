package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.UpdateInformationRequest;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.AccountAlreadyHasUserException;
import org.ghtk.todo_list.exception.EmailAlreadyExistedException;
import org.ghtk.todo_list.exception.EmailNotFoundException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.service.AuthUserService;

@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

  private final AuthUserRepository repository;

  @Override
  public AuthUser findById(String id) {
    log.info("(findById)id: {}", id);
    return repository.findById(id).orElseGet(() -> {
      log.error("(findById)id: {} not found", id);
      throw new UserNotFoundException();
    });
  }

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

  @Override
  public boolean existsByEmail(String email) {
    log.info("(existsByEmail)email: {}", email);
    return repository.existsByEmail(email);
  }

  @Override
  public Optional<AuthUser> findByAccountId(String accountId) {
    log.info("(findByAccountId)accountId: {}", accountId);
    return repository.findByAccountId(accountId);
  }

  @Override
  public AuthUserResponse updateUserDetail(String userId, UpdateInformationRequest request) {
    log.info("(updateUserDetail)userId: {}, request: {}", userId, request);
    var user = repository
        .findById(userId)
        .orElseThrow(() -> {
          log.error("(updateUserDetail)userId: {} not found", userId);
          throw new UserNotFoundException();
        });

    user.setFirstName(request.getFirstName());
    user.setMiddleName(request.getMiddleName());
    user.setLastName(request.getLastName());
    user.setPhone(request.getPhone());
    if (request.getDateOfBirth() != null && !request.getDateOfBirth().trim().isEmpty()) {
      user.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
    } else {
      user.setDateOfBirth(null);
    }
    user.setGender(request.getGender());
    user.setAddress(request.getAddress());
    repository.save(user);

    return AuthUserResponse.from(user);
  }
}
