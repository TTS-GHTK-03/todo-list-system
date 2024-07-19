package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.TaskService;

@Slf4j
@RequiredArgsConstructor
public class ActivityLogFacadeServiceImpl implements ActivityLogFacadeService {

  private final ActivityLogService activityLogService;
  private final ProjectService projectService;
  private final TaskService taskService;

  @Override
  public List<ActivityLog> getAllActivityLogsByTaskId(String userId, String projectId, String taskId) {
    log.info("(getAllActivityLogs)userId: {}, projectId: {}", userId, projectId);
    validateProjectId(projectId);
    validateProjectIdAndTaskId(projectId, taskId);
    return activityLogService.getAllActivityLogsByTaskId(taskId);
  }

  @Override
  public List<ActivityLog> getAllActivityLogsByUserId(String userId) {
    log.info("(getAllActivityLogsByUserId)userId: {}", userId);
    return activityLogService.getAllActivityLogsByUserId(userId);
  }

  void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {}", projectId);
      throw new ProjectNotFoundException();
    }
  }

  void validateProjectIdAndTaskId(String projectId, String taskId) {
    log.info("(validateProjectIdAndTaskId)projectId: {}, taskId: {}", projectId, taskId);
    if (!taskService.existByProjectIdAndTaskId(projectId, taskId)) {
      log.error("(validateProjectIdAndTaskId)taskId: {} not found", taskId);
      throw new TaskNotFoundException();
    }
  }
}
