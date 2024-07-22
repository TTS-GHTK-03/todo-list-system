package org.ghtk.todo_list.repository;

import java.util.Optional;
import org.ghtk.todo_list.entity.SprintProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintProgressRepository extends JpaRepository<SprintProgress, String> {

  @Query("""
        select sp from SprintProgress sp
        join Sprint s on s.id = sp.sprintId
        join Task t on t.sprintId = s.id
        where t.id = :taskId
      """)
  Optional<SprintProgress> findSprintProgressByTaskId(String taskId);

  SprintProgress findBySprintId(String sprintId);

  void deleteAllBySprintId(String sprintId);
}
