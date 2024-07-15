package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, String> {

}
