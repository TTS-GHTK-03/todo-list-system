package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, String> {

  String findUserIdByTaskId(String taskId);
}
