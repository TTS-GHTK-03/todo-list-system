package org.ghtk.todo_list.service;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.ProjectUser;

public interface ProjectUserService {

  ProjectUser createProjectUser(String userId, String projectId, String role);

  boolean existsByUserIdAndProjectId(String userId, String projectId);

  String getRoleProjectUser(String userId, String projectId);
}
