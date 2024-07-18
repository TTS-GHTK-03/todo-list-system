package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {

}
