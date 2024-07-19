package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;

public interface ActivityLogService {

  List<ActivityLog> getAllActivityLogsByTaskId(String taskId);
  List<ActivityLog> getAllActivityLogsByUserId(String userId);

  List<ActivityLog> getAllNotifications(String userId, int page);
  void deleteAllByTaskId(String taskId);
}
