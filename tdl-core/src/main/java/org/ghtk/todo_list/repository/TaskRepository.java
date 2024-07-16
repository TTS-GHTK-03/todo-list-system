package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

  @Query(value = "SELECT t FROM Task t WHERE t.projectId = :projectId")
  List<Task> getAllTasksByProjectId(String projectId);

  boolean existsById(String id);

  @Query("""
        select ta.userId from TaskAssignees ta
        join Task t on t.id = ta.taskId
        WHERE t.id = :taskId
          """)
  String getUserIdById(String taskId);

  @Query("""
    SELECT t FROM Task t 
    WHERE t.status != :taskStatusDone AND t.sprintId IN 
    (SELECT s.id FROM Sprint s WHERE s.status = :sprintStatusStart AND s.projectId = :projectId)
  """)
  List<Task> findAllByProjectId(String projectId, String taskStatusDone, String sprintStatusStart);

  Optional<Task> findByProjectIdAndId(String projectId, String id);
}
