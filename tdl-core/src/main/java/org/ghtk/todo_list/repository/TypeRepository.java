package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, String> {

  boolean existsByProjectIdAndTitle(String projectId, String title);
  boolean existsByIdAndProjectId(String typeId, String projectId);
  List<Type> findAllByProjectId(String projectId);
  void deleteAllByProjectId(String projectId);
  void deleteById(String typeId);

  Type findByProjectIdAndTitle(String projectId, String title);
}
