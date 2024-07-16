package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, String> {

  String findUserIdByTaskId(String taskId);

  @Query("""
      SELECT CASE WHEN EXISTS(
      SELECT ta FROM TaskAssignee ta
      JOIN Task t ON ta.taskId = t.id
      JOIN Sprint sp ON t.sprintId = sp.id
      JOIN Project p ON sp.projectId = p.id
      JOIN ProjectUser pu ON p.userId = pu.userId
      WHERE ta.userId = :userId
      AND ta.taskId = :taskId)
      THEN TRUE ELSE FALSE END
      FROM TaskAssignee ta
      """)
  boolean existsByUserIdAndTaskId(String userId, String taskId);
}
