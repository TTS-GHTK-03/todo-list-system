package org.ghtk.todo_list.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.repository.ActivityLogRepository;
import org.ghtk.todo_list.service.ActivityLogService;
import org.springframework.transaction.annotation.Transactional;

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
  public List<ActivityLog> getAllActivityLogsByUserId(String userId) {
    log.info("(getAllActivityLogsByUserId)userId: {}", userId);
    return activityLogRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
  }

  @Override
  public List<ActivityLog> getAllNotifications(String userId, int page) {
    log.info("(getAllNotifications)userId: {}, page: {}", userId, page);
    int offset = (page-1) * LIMIT;
    return activityLogRepository.findAllNotifications(userId, LIMIT, offset);
  }

  @Override
  @Transactional
  public void deleteAllByTaskId(String taskId) {
    log.info("(deleteAllByTaskId)taskId: {}", taskId);
    activityLogRepository.deleteAllByTaskId(taskId);
  }

  @Override
  public boolean existsByActivityLogId(String activityLogId) {
    log.info("(existsByActivityLogId)activityLogId: {}", activityLogId);
    return activityLogRepository.existsById(activityLogId);
  }

  @Override
  public boolean existsByActivityLogIdAndUserId(String activityLogId, String userId) {
    log.info("(existsByActivityLogIdAndUserId)activityLogId: {}, userId: {}", activityLogId, userId);
    return activityLogRepository.existsByIdAndUserId(activityLogId, userId);
  }

  @Override
  public void deleteById(String activityLogId) {
    log.info("(deleteById)activityLogId: {}", activityLogId);
    activityLogRepository.deleteById(activityLogId);
  }

  @Override
  public void create(ActivityLog activityLog) {
    log.info("(create)activityLog: {}", activityLog);
    activityLogRepository.save(activityLog);
  }
}
