package org.ghtk.todo_list.facade.imp;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.exception.ActivityLogNotExistedException;
import org.ghtk.todo_list.exception.ActivityLogNotFoundException;
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
      NotificationResponse notificationResponse = activityLogMapper.toNotificationResponse(activityLog, authUserService.getNameUserById(activityLog.getUserId()), sprintService.findById(activityLog.getSprintId()), taskService.findById(activityLog.getTaskId()));
      notificationResponseList.add(notificationResponse);
    }
    return notificationResponseList;
  }

  @Override
  public void deleteNotification(String userId, String projectId, String activityLogId) {
    log.info("(deleteNotification)userId: {}, projectId: {}, activityLogId: {}", userId, projectId, activityLogId);
    validateProjectId(projectId);
    validateActivityLogId(activityLogId);
    validateActivityLogIdAndUserId(activityLogId, userId);

    activityLogService.deleteById(activityLogId);
  }


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

  void validateActivityLogId(String activityLogId){
    log.info("(validateActivityLogId)activityLogId: {}", activityLogId);
    if(!activityLogService.existsByActivityLogId(activityLogId)){
      log.error("(validateActivityLogId)activityLogId: {} not found", activityLogId);
      throw new ActivityLogNotFoundException();
    }
  }

  void validateActivityLogIdAndUserId(String activityLogId, String userId){
    log.info("(validateActivityLogIdAndUserId)activityLogId: {}, userId: {}", activityLogId, userId);
    if(!activityLogService.existsByActivityLogIdAndUserId(activityLogId, userId)){
      log.error("(validateActivityLogIdAndUserId)activityLogId: {} not exists with userId {}", activityLogId, userId);
      throw new ActivityLogNotExistedException();
    }
  }
}
