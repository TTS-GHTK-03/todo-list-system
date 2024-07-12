package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.service.ProjectUserService;

@Slf4j
@RequiredArgsConstructor
public class ProjectUserServiceImpl implements ProjectUserService {

  private final ProjectUserRepository projectUserRepository;
  private final ProjectUserMapper projectUserMapper;

  @Override
  public ProjectUser createProjectUser(String userId, String projectId, String role,
      LocalDateTime createdAt, LocalDateTime lastUpdatedAt) {
    ProjectUser projectUser = projectUserMapper.toProjectUser(userId, projectId, role, createdAt, lastUpdatedAt);
    return projectUserRepository.save(projectUser);
  }
}
