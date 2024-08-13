package org.ghtk.todo_list.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;
import org.ghtk.todo_list.service.ShareTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("customUserScheduler")
@Slf4j
@RequiredArgsConstructor
public class UserScheduler {

  private final ProjectUserService projectUserService;
  private final AuthUserService authUserService;
  private final ProjectService projectService;
  private final ShareTokenService shareTokenService;
  private final EmailHelper emailHelper;

  @Scheduled(cron = "0 * * * * *")
  public void checkSprints() {
    List<ProjectUser> projectUsers = projectUserService.getAll();
    for (ProjectUser projectUser : projectUsers) {
      if (projectUser.getExpireDateShare() != null) {
        AuthUser authUser = authUserService.findByUserId(projectUser.getUserId());
        if(LocalDateTime.now().isAfter(projectUser.getExpireDateShare())) {
          String title = projectService.findTitleProjectById(projectUser.getProjectId());
          String fullName = setFullName(authUser);
          var subject = "Notice from the Todo List administrator in the project: " + title;
          var param = new HashMap<String, Object>();
          param.put("email", authUser.getEmail());
          param.put("title", fullName + " kicked you out of the project: " + title);
          param.put("subtitle",
              "Your participation in our Todo list project has expired. If you have any problems, please contact our manager.");
          emailHelper.send(subject, authUser.getEmail(), "email-kick-user-in-project-template",
              param);
          projectUserService.deleteById(projectUser.getId());
        }
      }
    }
  }

  private String setFullName(AuthUser user) {
    StringBuilder fullName = new StringBuilder();
    if(user.getFirstName() != null)
      fullName.append(capitalizeName(user.getFirstName())).append(" ");
    if(user.getMiddleName() != null)
      fullName.append(capitalizeName(user.getMiddleName())).append(" ");
    if(user.getLastName() != null)
      fullName.append(capitalizeName(user.getLastName()));

    return fullName.toString().trim();
  }

  private String capitalizeName(String name) {
    if (name == null || name.isEmpty()) {
      return "";
    }
    return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
  }
}
