package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    @Query(value = "SELECT t FROM Task t WHERE t.projectId = :projectId")
    List<Task> getAllTask(@Param("projectId") String projectId);

    @Query("SELECT COUNT(t) > 0 FROM Task t WHERE t.projectId = :projectId")
    boolean existsByProjectId(@Param("projectId") String projectId);
}
