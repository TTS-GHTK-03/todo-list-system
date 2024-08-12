package org.ghtk.todo_list.repository;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String>,
    JpaSpecificationExecutor<Project> {

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

  Project findByTitle(String title);
  Project findByKeyProject(String keyProject);

  boolean existsByTitle(String title);
  boolean existsByKeyProject(String keyProject);
  @Query("""
      SELECT p.title FROM Project p
      WHERE p.id = :projectId
  """)
  String findTitleProjectById(String projectId);
}
