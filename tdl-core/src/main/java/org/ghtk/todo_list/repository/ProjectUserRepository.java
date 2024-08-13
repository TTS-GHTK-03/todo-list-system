package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProjectUserRepository extends JpaRepository<ProjectUser, String> {

  @Query("""
      SELECT pu.role FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
      """)
  String getRoleProjectUser(@Param("userId") String userId, @Param("projectId") String projectId);

  @Modifying
  @Transactional
  @Query("""
      UPDATE ProjectUser pu SET pu.role = :role WHERE pu.userId = :memberId AND pu.projectId = :projectId
      """)
  void updateRoleByUserIdAndProjectId(String projectId, String memberId, String role);

  @Query("""
      SELECT pu FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
      """)
  ProjectUser existByUserIdAndProjectId(@Param("userId") String userId,
      @Param("projectId") String projectId);

  @Query("""
        select pu.projectId from ProjectUser pu
      """)
  List<String> findAllProjectId();

  @Query("""
    SELECT pu.role FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
    """)
  String findRoleByUserIdAndProjectId(String userId, String projectId);

  void deleteAllByProjectId(String projectId);

  @Modifying
  @Transactional
  @Query("""
      DELETE FROM ProjectUser pu WHERE pu.userId = :userId AND pu.projectId = :projectId
      """)
  void deleteByUserIdAndProjectId(String userId, String projectId);
}
