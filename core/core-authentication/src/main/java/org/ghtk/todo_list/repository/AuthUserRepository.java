package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, String> {

    @Query("SELECT user FROM AuthUser user WHERE user.email = :email")
    Optional<AuthUser> findByEmail(String email);

}
