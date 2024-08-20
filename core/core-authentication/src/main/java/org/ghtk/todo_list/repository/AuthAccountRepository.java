package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, String> {

  @Query("""
    select a from AuthAccount a 
    join AuthUser au on au.accountId = a.id
    where au.id = :userId
  """)
  Optional<AuthAccount> findFirstByUserId(String userId);

  boolean existsByUsername(String username);

  @Query("""
    select a from AuthAccount a
    join AuthUser au on au.accountId = a.id
    where au.email = :email
  """)
  Optional<AuthAccount> findByEmail(String email);
  Optional<AuthAccount> findByUsername(String username);

  @Modifying
  @Query("update AuthAccount a set a.isLockedPermanent = :isLockPermanent where a.id = :id")
  void updateLockPermanentById(String id, boolean isLockPermanent);

  @Query("""
    select a.username from AuthAccount a
    join AuthUser au on au.accountId = a.id
    where au.id = :userId
  """)
  String findUsernameByUserId(String userId);

}

