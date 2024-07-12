package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("""
            SELECT p FROM Project p 
            INNER JOIN ProjectUser pu 
            ON p.id = pu.projectId 
            WHERE pu.userId = :userId""")
    List<Project> getAllProject(@Param("userId") String userId);

    @Query("""
            SELECT p FROM Project p
            INNER JOIN ProjectUser pu 
            ON p.id = pu.projectId 
            WHERE pu.userId = :userId AND p.id = :projectId
            """)
    Project getProject(@Param("userId") String userId, @Param("projectId") String projectId);
}
