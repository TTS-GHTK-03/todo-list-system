package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.facade.TDLFacadeService;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TaskService;

@Slf4j
@RequiredArgsConstructor
public class TDLFacadeServiceImpl implements TDLFacadeService {

  private ProjectService projectService;
  private TaskService taskService;

  @Override
  public List<TaskResponse> getAllTaskByProjectId(String projectId) {
    log.info("(getAllTaskByProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      throw new ProjectNotFoundException();
    }
    return taskService.getAllTasksByProjectId(projectId);
  }
}
