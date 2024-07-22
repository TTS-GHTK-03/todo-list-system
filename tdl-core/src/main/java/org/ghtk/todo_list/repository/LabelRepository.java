package org.ghtk.todo_list.repository;

import java.util.List;
import org.ghtk.todo_list.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {

  boolean existsByTypeIdAndTitle(String typeId, String title);

  List<Label> findByTypeId(String typeId);

  void deleteAllByTypeId(String typeId);
}
