package org.ghtk.todo_list.mapper.impl;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectUserMapperImpl implements ProjectUserMapper {

  @Override
  public ProjectUser toProjectUser(String userId, String projectId, String role) {
    ProjectUser projectUser = new ProjectUser();
    projectUser.setUserId(userId);
    projectUser.setProjectId(projectId);
    projectUser.setRole(role);
    return projectUser;
  }
}
