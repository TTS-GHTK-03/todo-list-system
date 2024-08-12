package org.ghtk.todo_list.service;

import java.util.List;
import java.util.Optional;
import org.ghtk.todo_list.dto.request.UpdateInformationRequest;
import org.ghtk.todo_list.dto.response.AuthUserResponse;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.dto.response.UserResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.repository.UserProjection;

public interface AuthUserService {

  AuthUser findById(String id);
  AuthUser create(String email, String accountId);
  AuthUser createTemporaryUser(String email);
  boolean existsByEmail(String email);
  boolean existById(String id);
  Optional<AuthUser> findByAccountId(String accountId);
  String getUserId(String email);
  AuthUserResponse updateUserDetail(String userId, UpdateInformationRequest request);
  AuthUserResponse getDetail(String userId);
  UserProjection getByUserId(String userId);
  List<UserNameResponse> getNameUser(String projectId);
  UserNameResponse getNameUserById(String userId);
  AuthUser create(String lastName);
  AuthUser findByUnassigned();
  List<UserResponse> getAllUserByProject(String projectId);
  UserResponse getUserResponseById(String userId);
  AuthUser findByEmail(String email);
  boolean existsByEmailAndAccountId(String email, String accountId);
  void saveUserShare(String email, String accountId);
  AuthUser findByUserId(String userId);
}
