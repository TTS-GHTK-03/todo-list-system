package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String>,
    JpaSpecificationExecutor<Task> {

  @Query(value = "SELECT t FROM Task t WHERE t.projectId = :projectId")
  List<Task> getAllTasksByProjectId(String projectId);

  @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND t.status = :status")
  List<Task> getAllTasksByProjectIdAndStatus(String projectId, String status);

  boolean existsById(String id);

  @Query("""
      select ta.userId from TaskAssignees ta
      join Task t on t.id = ta.taskId
      WHERE t.id = :taskId
        """)
  String getUserIdById(String taskId);

  @Query("""
        SELECT t FROM Task t 
        WHERE t.status != 'DONE'AND t.status != 'TODO' AND t.sprintId IN 
        (SELECT s.id FROM Sprint s WHERE s.status = :sprintStatusStart AND s.projectId = :projectId)
      """)
  List<Task> findAllByProjectId(String projectId, String sprintStatusStart);

  Optional<Task> findByProjectIdAndId(String projectId, String id);

  @Query("""
      SELECT CASE WHEN EXISTS (
          SELECT 1
          FROM ProjectUser pu
          JOIN Project p ON pu.projectId = p.id
          JOIN Task t ON pu.projectId = t.projectId
          WHERE pu.userId = :userId
          AND t.id = :taskId
      ) THEN TRUE ELSE FALSE END
      """)
  boolean existsByUserIdAndTaskId(@Param("userId") String userId, @Param("taskId") String taskId);

  @Modifying
  @Transactional
  @Query("UPDATE Task t SET t.point = :point WHERE t.id = :taskId")
  void updatePoint(@Param("taskId") String taskId, @Param("point") int point);

  List<Task> findAllBySprintId(String sprintId);

  boolean existsBySprintId(String sprintId);

  boolean existsByProjectIdAndId(String projectId, String id);

  void deleteAllByProjectId(String projectId);

  void deleteAllBySprintId(String sprintId);

  @Transactional
  @Modifying
  @Query("""
      UPDATE Task t SET t.typeId = :defaultTypeId WHERE t.typeId = :oldTypeId
      """)
  void updateTaskTypeIdByTypeId(String defaultTypeId, String oldTypeId);

  @Modifying
  @Query("UPDATE Task s SET s.title = :title WHERE s.id = :id")
  void updateTitleById(String id, String title);


  boolean existsByTypeId(String typeId);

  @Query("""
      SELECT t FROM Task t WHERE t.projectId = :projectId ORDER BY t.createdAt DESC LIMIT 1
      """)
  Task getTaskLastestByProjectId(String projectId);

  @Query("""
      SELECT t FROM Task t 
      JOIN TaskAssignees ta ON t.id = ta.taskId AND ta.userId = :userId
      WHERE t.status != :taskStatusDone ORDER BY t.status, t.createdAt
      """)
  List<Task> getAllTaskAssigneesForUser(String userId, String taskStatusDone);

  @Query("SELECT COUNT(s) FROM Task s WHERE s.sprintId = :sprintId AND s.projectId = :projectId AND s.status <> 'DONE'")
  Integer countBySprintIdAndProjectIdAndStatusNotDone(String sprintId, String projectId);

  @Query("SELECT COUNT(s) FROM Task s WHERE s.sprintId = :sprintId AND s.projectId = :projectId AND s.status = 'DONE'")
  Integer countBySprintIdAndProjectIdAndStatusDone(String sprintId, String projectId);

  @Query("SELECT t FROM Task t WHERE t.projectId = :projectId AND t.sprintId = :sprintId AND t.status <> 'DONE'")
  List<Task> findAllByProjectIdAndSprintIdAndStatusNotDone(String projectId, String sprintId);

}
