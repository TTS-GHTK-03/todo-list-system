package org.ghtk.todo_list.mapper;

import java.time.LocalDateTime;
import org.ghtk.todo_list.entity.ProjectUser;

public interface ProjectUserMapper {
  ProjectUser toProjectUser(String userId, String projectId, String role);
}
