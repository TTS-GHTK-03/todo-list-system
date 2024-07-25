package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.LabelAttached;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelAttachedRepository extends JpaRepository<LabelAttached, String> {
  void deleteByLabelId(String labelId);
  void deleteAllByTaskId(String taskId);
  boolean existsByLabelIdAndTaskId(String labelId, String taskId);

  List<LabelAttached> findByTaskId(String taskId);
  void deleteAllByLabelId(String labelId);
  boolean existsByTaskIdAndId(String taskId, String labelAttachedId);
}
