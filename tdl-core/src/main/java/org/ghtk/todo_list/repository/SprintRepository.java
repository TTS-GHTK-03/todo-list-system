package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, String> {

  @Query("""
      SELECT sp FROM Sprint sp
      WHERE sp.projectId = :projectId
      """)
  List<Sprint> findByProjectId(String projectId);

  @Query("""
      SELECT sp FROM Sprint sp
      WHERE sp.projectId = :projectId AND sp.status = :status
      """)
  List<Sprint> findByProjectIdAndStatus(String projectId, String status);

  boolean existsByProjectIdAndTitle(String projectId, String title);

  void deleteAllByProjectId(String projectId);
}
