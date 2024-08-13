package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.SprintProgress;

public interface SprintProgressService {

  SprintProgress save(SprintProgress sprintProgress);
  void updateCompleteTask(String taskId);
  SprintProgress findBySprintId(String sprintId);
  void deleteAllBySprintId(String sprintId);

}
