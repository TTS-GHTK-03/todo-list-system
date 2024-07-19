package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.repository.ActivityLogRepository;
import org.ghtk.todo_list.service.ActivityLogService;

@Slf4j
@RequiredArgsConstructor
public class ActivityLogServiceImpl implements ActivityLogService {

  private final ActivityLogRepository activityLogRepository;
  private final static int LIMIT = 20;

  @Override
  public List<ActivityLog> getAllActivityLogsByTaskId(String taskId) {
    log.info("(getAllActivityLogsByTaskId)taskId: {}", taskId);
    return activityLogRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId);
  }

  @Override
  public List<ActivityLog> getAllNotifications(String userId, int page) {
    log.info("(getAllNotification)userId: {}, page: {}", userId, page);
    int offset = (page-1) * LIMIT;
    return activityLogRepository.findAllNotifications(userId, LIMIT, offset);
  }
}
