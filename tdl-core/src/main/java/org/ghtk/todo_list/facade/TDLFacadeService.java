package org.ghtk.todo_list.facade;

import org.ghtk.todo_list.repository.TaskRepository;

public interface TDLFacadeService {

  public void existsByProjectId(TaskRepository taskRepository, String projectId);
}
