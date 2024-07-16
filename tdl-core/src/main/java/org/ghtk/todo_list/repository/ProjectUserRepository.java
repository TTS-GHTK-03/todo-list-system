package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, String> {

  @Query("""
      SELECT pu FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
      """)
  String getRoleProjectUser(@Param("userId") String userId, @Param("projectId") String projectId);

  @Query("""
      SELECT pu FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
      """)
  ProjectUser existByUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") String projectId);

  @Query("""
    select pu.projectId from ProjectUser pu
    where pu.role = :role AND pu.userId IN 
    (SELECT s.id FROM AuthUser s)
  """)
  List<String> findProjectIdByUserId(String role);
}
