package org.ghtk.todo_list.scheduler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.repository.ActivityLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("customHistoryScheduler")
@Slf4j
@RequiredArgsConstructor
public class HistoryScheduler {

  private final ActivityLogRepository activityLogRepository;

  @Scheduled(cron = "0 0 0 * * ?")
  public void deleteOldActivityLogs() {
    log.info("(deleteOldActivityLogs)Deleting old activity logs");
    LocalDateTime cutoffDate = LocalDateTime.now().minus(30, ChronoUnit.DAYS);
    activityLogRepository.deleteOldActivityLogs(cutoffDate);
  }
}
