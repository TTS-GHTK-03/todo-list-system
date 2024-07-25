package org.ghtk.todo_list.scheduler;

import static org.ghtk.todo_list.constant.CacheConstant.OTP_TTL_MINUTES;
import static org.ghtk.todo_list.constant.SprintStatus.START;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Sprint;
import org.ghtk.todo_list.repository.AuthUserRepository;
import org.ghtk.todo_list.repository.SprintRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("customSprintScheduler")
@Slf4j
@RequiredArgsConstructor
public class SprintScheduler {

  private final SprintRepository sprintRepository;
  private final AuthUserRepository authUserRepository;
  private final EmailHelper emailHelper;

  @Scheduled(cron = "0 0 6 * * ?")
  public void checkSprints() {
    List<Sprint> sprints = sprintRepository.findAll();
    LocalDate today = LocalDate.now();

    for (Sprint sprint : sprints) {
      LocalDate startDate = sprint.getStartDate();
      if (startDate.isEqual(today.plusDays(1))) {
        List<AuthUser> users = authUserRepository.getAllUserByProject(sprint.getProjectId());

        for (AuthUser user : users) {
          String subject = "Sprint Starting Soon";
          String text = "The sprint " + sprint.getTitle() + " will start tomorrow.";
          emailHelper.send(subject, user.getEmail(), text);
        }
      } else if (startDate.isEqual(today)) {
        sprint.setStatus(START.toString());
        sprintRepository.save(sprint);
      }
    }
  }

}
