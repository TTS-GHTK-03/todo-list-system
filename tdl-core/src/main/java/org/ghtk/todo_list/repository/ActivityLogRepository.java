package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, String> {

  List<ActivityLog> findAllByTaskIdOrderByCreatedAtDesc(String taskId);
  List<ActivityLog> findAllByUserIdOrderByCreatedAtDesc(String userId);
}
