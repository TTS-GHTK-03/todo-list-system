package org.ghtk.todo_list.facade.imp;

import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.repository.TaskRepository;

public class TDLFacadeService implements
    org.ghtk.todo_list.facade.TDLFacadeService {


  public void existsByProjectId(TaskRepository taskRepository, String projectId) {
    if (!taskRepository.existsByProjectId(projectId)) {
      throw new ProjectNotFoundException();
    }
  }
}
