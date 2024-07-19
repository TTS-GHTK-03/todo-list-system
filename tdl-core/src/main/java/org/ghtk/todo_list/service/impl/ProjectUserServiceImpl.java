package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.ProjectUserNotFoundException;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.service.ProjectUserService;

@Slf4j
@RequiredArgsConstructor
public class ProjectUserServiceImpl implements ProjectUserService {

  private final ProjectUserRepository projectUserRepository;
  private final ProjectUserMapper projectUserMapper;

  @Override
  public ProjectUser createProjectUser(String userId, String projectId, String role) {
    log.info("(createProjectUser)project user: {}", userId);
    ProjectUser projectUser = projectUserMapper.toProjectUser(userId, projectId, role);
    return projectUserRepository.save(projectUser);
  }

  @Override
  public boolean existsByUserIdAndProjectId(String userId, String projectId) {
    log.info("(existsByUserIdAndProjectId)userId: {}, projectId: {}", userId, projectId);
    if(projectUserRepository.existByUserIdAndProjectId(userId, projectId) != null)
      return true;
    return false;
  }

  @Override
  public String getRoleProjectUser(String userId, String projectId) {
    log.info("(getRoleProjectUser)user: {}, project: {}", userId, projectId);
    if(projectUserRepository.existByUserIdAndProjectId(userId, projectId) == null){
      log.error("(getProjectInformation)project: {}", projectId);
      throw new ProjectUserNotFoundException();
    }
    return projectUserRepository.getRoleProjectUser(userId, projectId);
  }
}
