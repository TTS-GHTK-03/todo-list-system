package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.request.UpdateInformationRequest;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.exception.AccountAlreadyHasUserException;
import org.ghtk.todo_list.exception.EmailAlreadyExistedException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.mapper.UserResponseMapper;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.repository.UserProjection;
import org.ghtk.todo_list.service.AuthUserService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

  private final AuthUserRepository repository;
  private final UserResponseMapper userResponseMapper;
  private static final String ADMIN_ROLE = "ADMIN";
  private static final String UNASSIGNED = "Unassigned";

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
  public AuthUser createTemporaryUser(String email) {
    log.info("(createTemporaryUser)email: {}", email);
    if (repository.existsByEmail(email)) {
      log.error("(createTemporaryUser)email: {} already exist", email);
      throw new EmailAlreadyExistedException(email);
    }

    return repository.save(AuthUser.from(email));
  }


  @Override
  public boolean existsByEmail(String email) {
    log.info("(existsByEmail)email: {}", email);
    return repository.existsByEmail(email);
  }

  @Override
  public boolean existById(String id) {
    log.info("(existsById)id: {}", id);
    return repository.existsById(id);
  }

  @Override
  public Optional<AuthUser> findByAccountId(String accountId) {
    log.info("(findByAccountId)accountId: {}", accountId);
    return repository.findByAccountId(accountId);
  }

  @Override
  public String getUserId(String email) {
    log.info("(getUserId)email: {}", email);
    return repository.getUserId(email);
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
    user.setGender(request.getGender().toUpperCase());
    user.setAddress(request.getAddress());
    repository.save(user);

    return AuthUserResponse.from(user);
  }

  @Override
  public AuthUserResponse getDetail(String userId) {
    log.info("(getDetail)userId: {}", userId);
    return AuthUserResponse.from(repository
        .findById(userId)
        .orElseThrow(() -> {
          log.error("(getDetail)userId: {} not found", userId);
          throw new UserNotFoundException();
        }));
  }

  @Override
  public List<UserNameResponse> getNameUser(String projectId) {
    log.info("(getNameUser)projectId: {}", projectId);
    return repository.getNameUser(ADMIN_ROLE, projectId);
  }

  @Override
  public UserNameResponse getNameUserById(String userId) {
    log.info("(getNameUserById)userId: {}", userId);
    if(!repository.existsById(userId)){
      log.error("(getNameUserById)userId: {} not found", userId);
      throw new UserNotFoundException();
    }
    return repository.getNameUserById(userId);
  }

  @Override
  public AuthUser create(String lastName) {
    log.info("(create)lastName: {}", lastName);
    AuthUser authUser = new AuthUser();
    authUser.setLastName(lastName);
    return repository.save(authUser);
  }

  @Override
  public AuthUser findByUnassigned() {
    log.info("(findByUnassigned)");
    return repository.findByLastName(UNASSIGNED);
  }

  @Override
  public List<UserResponse> getAllUserByProject(String projectId) {
    log.info("(getAllUserByProject)projectId: {}", projectId);
    List<AuthUser> authUserList = repository.getAllUserByProject(projectId);
    return userResponseMapper.toUserResponseList(authUserList);
  }

  @Override
  public UserResponse getUserResponseById(String userId) {
    log.info("(getUserResponseById)userId: {}", userId);
    return userResponseMapper.toUserResponse(repository.findById(userId).get());
  }

  @Override
  public AuthUser findByEmail(String email) {
    log.info("(findByEmail)email: {}", email);
    return repository
        .findByEmail(email)
        .orElseThrow(() -> {
          log.error("(findByEmail)email: {} not found", email);
          throw new UserNotFoundException();
        });
  }

  @Override
  public boolean existsByEmailAndAccountId(String email, String accountId) {
    log.info("(existsByEmailAndAccountId)email: {}, accountId: {}", email, accountId);
    return repository.existsByEmailAndAccountId(email, accountId);
  }

  @Override
  @Transactional
  public void saveUserShare(String email, String accountId) {
    log.info("(save)email: {}, accountId: {}", email, accountId);
    repository.updateUserByEmail(email, accountId);
  }

  @Override
  public AuthUser findByUserId(String userId) {
    log.info("(findByUserId)userId: {}", userId);
    return repository.getByUserId(userId);
  }

  @Override
  public UserProjection getByUserId(String userId) {
    log.info("(getByUserId)userId: {}", userId);
    return repository.findByUserId(userId)
        .orElseThrow(() -> {
          log.error("(getByUserId)userId: {} not found", userId);
          throw new UserNotFoundException();
        });
  }

}
