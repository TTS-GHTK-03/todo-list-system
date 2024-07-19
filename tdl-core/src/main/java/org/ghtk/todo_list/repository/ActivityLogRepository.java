package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, String> {

  List<ActivityLog> findAllByTaskIdOrderByCreatedAtDesc(String taskId);

  @Query("""
      SELECT DISTINCT al FROM ActivityLog al
      JOIN TaskAssignees ta ON al.taskId = ta.taskId and ta.userId = :userId
      join Sprint s on al.sprintId = s.id order by al.createdAt DESC LIMIT :end OFFSET :start
      """)
  List<ActivityLog> findAllNotifications(String userId, int end, int start);
}
