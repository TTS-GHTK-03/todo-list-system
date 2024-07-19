package org.ghtk.todo_list.service;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;

public interface ActivityLogService {

  List<ActivityLog> getAllActivityLogsByTaskId(String taskId);
}
