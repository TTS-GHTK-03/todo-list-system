package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

  @Query(value = "SELECT t FROM Task t WHERE t.projectId = :projectId")
  List<Task> getAllTasksByProjectId(String projectId);

  boolean existsByProjectId(String projectId);

  boolean existsById(String id);

  @Query(value = "SELECT t FROM Task t WHERE t.id = :id")
  Task getTaskByTaskId(String id);
}
