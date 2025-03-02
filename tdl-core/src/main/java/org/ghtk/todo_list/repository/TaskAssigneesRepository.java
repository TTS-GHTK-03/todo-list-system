package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.TaskAssignees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskAssigneesRepository extends JpaRepository<TaskAssignees, String> {

  @Query("SELECT t.userId FROM TaskAssignees t WHERE t.taskId = :taskId")
  String findUserIdByTaskId(String taskId);

  boolean existsByUserIdAndTaskId(String userId, String taskId);

  void deleteAllByTaskId(String taskId);

  TaskAssignees findByTaskId(String taskId);

  @Modifying
  @Transactional
  @Query("""
        UPDATE TaskAssignees ta SET ta.userId = :userId
        WHERE ta.userId = :memberId AND ta.taskId IN (SELECT t.id FROM Task t WHERE t.projectId = :projectId)
        """)
  void updateTaskAssigneesByUserIdAndProjectId(@Param("userId") String userId,
      @Param("memberId") String memberId, @Param("projectId") String projectId);

}
