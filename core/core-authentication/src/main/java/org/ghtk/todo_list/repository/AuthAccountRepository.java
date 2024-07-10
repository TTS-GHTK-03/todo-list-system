package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

