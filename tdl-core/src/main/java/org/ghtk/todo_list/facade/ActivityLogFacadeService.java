package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.model.response.NotificationResponse;

public interface ActivityLogFacadeService {

  List<NotificationResponse> getAllNotifications(String userId, String projectId, int page);
  void deleteNotification(String userId, String projectId, String activityLogId);
  List<ActivityLog> getAllActivityLogsByTaskId(String userId, String projectId, String taskId);
  List<ActivityLog> getAllActivityLogsByUserId(String userId);

}
