package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query(value = "SELECT * FROM project p INNER JOIN project_user pu ON p.id = pu.project_id WHERE pu.user_id = :userId", nativeQuery = true)
    List<Project> getAllProjectByUser(@Param("userId") String userId);

    @Query(value = "SELECT * FROM project p INNER JOIN project_user pu ON p.id = pu.project_id WHERE pu.user_id = :userId AND p.id = :projectId", nativeQuery = true)
    Project getProjectByUser(@Param("userId") String userId, @Param("projectId") String projectId);
}
