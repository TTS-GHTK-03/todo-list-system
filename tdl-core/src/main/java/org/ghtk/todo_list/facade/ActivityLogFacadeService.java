package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.ActivityLog;

public interface ActivityLogFacadeService {

  List<ActivityLog> getAllActivityLogs(String userId, String projectId);
}
