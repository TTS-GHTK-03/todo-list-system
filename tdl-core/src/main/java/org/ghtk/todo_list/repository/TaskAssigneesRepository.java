package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, String> {

  @Query("SELECT t.userId FROM TaskAssignees t WHERE t.taskId = :taskId")
  String findUserIdByTaskId(String taskId);

  boolean existsByUserIdAndTaskId(String userId, String taskId);
  void deleteAllByTaskId(String taskId);
}
