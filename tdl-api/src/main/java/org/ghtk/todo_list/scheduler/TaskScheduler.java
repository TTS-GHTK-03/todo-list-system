package org.ghtk.todo_list.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.repository.TaskRepository;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.TaskAssigneesService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("customTaskScheduler")
@Slf4j
@RequiredArgsConstructor
public class TaskScheduler {

  public static final String SPRINT_STATUS_START = "START";

  private final EmailHelper emailHelper;
  private final TaskRepository taskRepository;
  private final ProjectUserRepository projectRepository;
  private final AuthUserService authUserService;
  private final TaskAssigneesService taskAssigneesService;

  @Scheduled(cron = "0 0 6 * * *")
  private void checkTasks() {
    log.info("(checkTasks)Check for tasks that are about to expire");
    List<Task> allTasks = new ArrayList<>();
    List<String> ids = projectRepository.findAllProjectId();

    for (String id : ids) {
      List<Task> tasks = getTasksFromSprint(id);
      allTasks.addAll(tasks);
    }

    for (Task task : allTasks) {
      LocalDate now = LocalDate.now();
      long daysUntilDue = ChronoUnit.DAYS.between(now, task.getDueDate());

      if (daysUntilDue == 1 && !task.getIsSendMail()) {
        var userId = taskAssigneesService.findUserIdByTaskId(task.getId());
        var user = authUserService.getByUserId(userId);
        String email = user.getEmail();
        String subject = "Task Due Soon: " + task.getTitle();
        String text = "The task \"" + task.getTitle() + "\" is due in 1 day.";

        emailHelper.send(email, subject, text);

        task.setIsSendMail(true);
        taskRepository.save(task);
      }
    }
  }

  private List<Task> getTasksFromSprint(String projectId) {
    log.info("(getTasksFromSprint)projectId: {}", projectId);
    return taskRepository.findAllByProjectId(projectId, SPRINT_STATUS_START);
  }
}
