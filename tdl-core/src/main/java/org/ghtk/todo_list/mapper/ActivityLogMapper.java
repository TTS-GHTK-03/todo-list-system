package org.ghtk.todo_list.mapper;

import org.ghtk.todo_list.dto.response.ActiveLoginResponse;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.model.response.NotificationResponse;

public interface ActivityLogMapper {

  NotificationResponse toNotificationResponse(ActivityLog activityLog,
      UserNameResponse userNameResponse, Sprint sprint, Task task);
}
