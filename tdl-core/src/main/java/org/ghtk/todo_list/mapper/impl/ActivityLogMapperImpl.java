package org.ghtk.todo_list.mapper.impl;

import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.mapper.ActivityLogMapper;
import org.ghtk.todo_list.model.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityLogMapperImpl implements ActivityLogMapper {

  @Override
  public NotificationResponse toNotificationResponse(ActivityLog activityLog,
      UserNameResponse userNameResponse, Sprint sprint, Task task) {
    NotificationResponse notificationResponse = new NotificationResponse();
    notificationResponse.setId(activityLog.getId());
    notificationResponse.setAction(activityLog.getAction());
    notificationResponse.setCreatedAt(activityLog.getCreatedAt());
    notificationResponse.setUserNameResponse(userNameResponse);
    notificationResponse.setSprintTitle(sprint.getTitle());
    notificationResponse.setTaskTitle(task.getTitle());
    return notificationResponse;
  }
}
