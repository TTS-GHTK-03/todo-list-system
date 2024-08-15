package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.Label;
import org.ghtk.todo_list.model.response.LabelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {

  boolean existsByTypeIdAndTitle(String typeId, String title);

  List<Label> findByTypeId(String typeId);

  @Query("""
      SELECT l FROM Label l
      JOIN LabelAttached la ON la.labelId = l.id
      JOIN Type t ON t.id = l.typeId AND t.projectId = :projectId
      """)
  List<Label> getAllLabelByProjectIdAndLabelAttached(String projectId);

  @Query("""
      SELECT l FROM Label l
      JOIN LabelAttached la ON la.labelId = l.id
      JOIN Task t ON t.id = la.taskId AND t.id = :taskId
      """)
  List<Label> getAllLabelByTask(String taskId);

  void deleteAllByTypeId(String typeId);

  void deleteByTypeId(String typeId);

  boolean existsByTypeId(String typeId);

  boolean existsByTypeIdAndId(String typeId, String id);
}
