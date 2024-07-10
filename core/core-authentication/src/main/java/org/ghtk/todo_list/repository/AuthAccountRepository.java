package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.AuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthAccountRepository extends JpaRepository<AuthAccount, String> {

  Optional<AuthAccount> findFirstByUserId(String userId);

  boolean existsByUsername(String username);
}

