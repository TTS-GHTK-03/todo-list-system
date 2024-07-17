package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

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
    select new org.ghtk.todo_list.repository.UserProjection(a.firstName, a.middleName, a.lastName, a.email) from AuthUser a 
    where a.id = :userId
  """)
  Optional<UserProjection> findByUserId(String userId);

}
