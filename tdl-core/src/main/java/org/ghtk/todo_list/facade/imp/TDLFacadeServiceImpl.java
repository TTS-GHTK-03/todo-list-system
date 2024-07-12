package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.facade.TDLFacadeService;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TDLFacadeServiceImpl implements TDLFacadeService {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private TaskService taskService;

  @Override
  public List<TaskResponse> getAllTaskByProjectId(String projectId) {
    log.info("(getAllTaskByProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      throw new ProjectNotFoundException();
    }
    return taskService.getAllTasksByProjectId(projectId);
  }

  @Override
  public TaskResponse getTaskByTaskId(String projectId, String taskId) {
    log.info("(getTaskByTaskId)taskId: {}", taskId);
    if (!projectService.existById(projectId)) {
      throw new ProjectNotFoundException();
    }
    if (!taskService.existsById(taskId)) {
      throw new TaskNotFoundException();
    }

    return taskService.getTaskByTaskId(taskId);
  }
}
