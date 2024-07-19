package org.ghtk.todo_list.facade.imp;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.TaskNotFoundException;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.ghtk.todo_list.mapper.ActivityLogMapper;
import org.ghtk.todo_list.model.response.NotificationResponse;
import org.ghtk.todo_list.model.response.SprintResponse;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.SprintService;
import org.ghtk.todo_list.service.TaskService;

@Slf4j
@RequiredArgsConstructor
public class ActivityLogFacadeServiceImpl implements ActivityLogFacadeService {

  private final ActivityLogService activityLogService;
  private final AuthUserService authUserService;
  private final ProjectService projectService;
  private final SprintService sprintService;
  private final TaskService taskService;
  private final ActivityLogMapper activityLogMapper;

  @Override
  public List<NotificationResponse> getAllNotifications(String userId, String projectId, int page) {
    log.info("(getAllNotifications)userId: {}, projectId: {}, page: {}", userId, projectId, page);
    validateProjectId(projectId);
    List<ActivityLog> activityLogList = activityLogService.getAllNotifications(userId, page);

    List<NotificationResponse> notificationResponseList = new ArrayList<>();
    for(ActivityLog activityLog : activityLogList){
      UserNameResponse userNameResponse = authUserService.getNameUserById(activityLog.getUserId());
      Sprint sprint = sprintService.findById(activityLog.getSprintId());
      Task task = taskService.findById(activityLog.getTaskId());

      NotificationResponse notificationResponse = activityLogMapper.toNotificationResponse(activityLog, userNameResponse, sprint, task);
      notificationResponseList.add(notificationResponse);
    }
    return notificationResponseList;
  }

  @Override
  public List<ActivityLog> getAllActivityLogsByTaskId(String userId, String projectId, String taskId) {
    log.info("(getAllActivityLogs)userId: {}, projectId: {}", userId, projectId);
    validateProjectId(projectId);
    validateProjectIdAndTaskId(projectId, taskId);
    return activityLogService.getAllActivityLogsByTaskId(taskId);
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
