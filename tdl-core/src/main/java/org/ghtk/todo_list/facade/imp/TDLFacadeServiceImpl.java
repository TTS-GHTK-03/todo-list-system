package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.TaskStatus;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.StatusTaskInvalidException;
import org.ghtk.todo_list.facade.TDLFacadeService;
import org.ghtk.todo_list.model.response.TaskResponse;
import org.ghtk.todo_list.service.AuthUserService;
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
  private final AuthUserService authUserService;

  @Override
  public List<TaskResponse> getAllTaskByProjectId(String userId, String projectId) {
    log.info("(getAllTaskByProjectId)projectId: {}", projectId);
    validateProjectId(projectId);
    return taskService.getAllTasksByProjectId(projectId);
  }

  @Override
  public TaskResponse getTaskByTaskId(String userId, String projectId, String taskId) {
    log.info("(getTaskByTaskId)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    String id = taskService.getUserIdById(taskId);
    return taskService.findById(taskId, authUserService.getByUserId(id));
  }

  @Override
  public TaskResponse updateStatusTask(String userId, String projectId, String taskId,
      String status) {
    log.info("(updateStatusTask)taskId: {},projectId: {}", taskId, projectId);
    validateProjectId(projectId);
    if(!TaskStatus.isValid(status)) {
      log.error("(updateStatusTask)taskId: {},projectId: {}", taskId, projectId);
      throw new StatusTaskInvalidException();
    }
    String id = taskService.getUserIdById(taskId);
    return taskService.updateStatus(taskId, status.toUpperCase(), authUserService.getByUserId(id));
  }

  void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {}", projectId);
      throw new ProjectNotFoundException();
    }
  }
}
