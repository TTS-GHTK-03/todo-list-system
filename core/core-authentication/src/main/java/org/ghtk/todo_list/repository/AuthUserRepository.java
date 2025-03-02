package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String>,
    JpaSpecificationExecutor<AuthUser> {

  boolean existsByEmail(String email);

  boolean existsByAccountId(String accountId);

  Optional<AuthUser> findByAccountId(String accountId);

  @Query("""
      SELECT u.id FROM AuthUser u WHERE u.email = :email
      """)
  String getUserId(String email);

  @Query("""
      SELECT new org.ghtk.todo_list.dto.response.UserNameResponse(u.firstName, u.middleName, u.lastName)
      FROM AuthUser u JOIN ProjectUser pu ON u.id = pu.userId
      WHERE pu.role = :role AND pu.projectId = :projectId
      """)
  List<UserNameResponse> getNameUser(String role, String projectId);

  @Query("""
      SELECT new org.ghtk.todo_list.dto.response.UserNameResponse(u.firstName, u.middleName, u.lastName)
      FROM AuthUser u WHERE u.id = :userId
      """)
  UserNameResponse getNameUserById(String userId);

  @Query("""
    select new org.ghtk.todo_list.repository.UserProjection(a.firstName, a.middleName, a.lastName, a.email) from AuthUser a 
    where a.id = :userId
  """)
  Optional<UserProjection> findByUserId(String userId);
  AuthUser findByLastName(String lastName);

  @Query("""
      SELECT au FROM AuthUser au 
      JOIN ProjectUser pu ON pu.userId = au.id AND pu.projectId = :projectId 
      WHERE au.email != ''
      """)
  List<AuthUser> getAllUserByProject(String projectId);

  Optional<AuthUser> findByEmail(String email);

  boolean existsByEmailAndAccountId(String email, String accountId);

  @Modifying
  @Transactional
  @Query("""
    update AuthUser au set au.accountId = :accountId where au.email = :email
  """)
  void updateUserByEmail(String email, String accountId);

  @Query("""
    select au from AuthUser au where au.id = :userId
  """)
  AuthUser getByUserId(String userId);
}
