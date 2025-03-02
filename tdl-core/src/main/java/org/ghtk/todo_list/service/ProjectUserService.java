package org.ghtk.todo_list.service;

import java.time.LocalDateTime;
import java.util.List;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;

public interface ProjectUserService {

  ProjectUser createProjectUser(String userId, String projectId, String role);

  ProjectUser createProjectUserShare(ProjectUser projectUser);

  boolean existsByUserIdAndProjectId(String userId, String projectId);

  String getRoleProjectUser(String userId, String projectId);

  String updateRoleProjectUser(String projectId, String memberId, String role);

  void deleteAllByProjectId(String projectId);

  void deleteByUserIdAndProjectId(String memberId, String projectId);

  List<ProjectUser> getAll();

  void deleteById(String id);
}
