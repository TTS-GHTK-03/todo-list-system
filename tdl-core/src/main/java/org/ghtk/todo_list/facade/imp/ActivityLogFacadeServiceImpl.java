package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ActivityLog;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.facade.ActivityLogFacadeService;
import org.ghtk.todo_list.service.ActivityLogService;
import org.ghtk.todo_list.service.ProjectService;

@Slf4j
@RequiredArgsConstructor
public class ActivityLogFacadeServiceImpl implements ActivityLogFacadeService {

  private final ActivityLogService activityLogService;
  private final ProjectService projectService;

  @Override
  public List<ActivityLog> getAllActivityLogs(String userId, String projectId) {
    log.info("(getAllActivityLogs)userId: {}, projectId: {}", userId, projectId);
    validateProjectId(projectId);
    return activityLogService.getAllActivityLogs();
  }

  void validateProjectId(String projectId) {
    log.info("(validateProjectId)projectId: {}", projectId);
    if (!projectService.existById(projectId)) {
      log.error("(validateProjectId)projectId: {}", projectId);
      throw new ProjectNotFoundException();
    }
  }
}
