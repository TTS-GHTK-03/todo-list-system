package org.ghtk.todo_list.repository;

import org.ghtk.todo_list.entity.LabelAttached;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelAttachedRepository extends JpaRepository<LabelAttached, String> {
  void deleteByLabelId(String labelId);
  boolean existsByLabelIdAndTaskId(String labelId, String taskId);
}
