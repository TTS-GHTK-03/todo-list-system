package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.ProjectUserNotFoundException;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.service.ProjectUserService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor

public class ProjectUserServiceImpl implements ProjectUserService {

  private final ProjectUserRepository projectUserRepository;
  private final ProjectUserMapper projectUserMapper;

  @Override
  public ProjectUser createProjectUser(String userId, String projectId, String role) {
    log.info("(createProjectUser)user: {}, project: {}, role: {}", userId, projectId, role);
    ProjectUser projectUser = projectUserMapper.toProjectUser(userId, projectId, role);
    return projectUserRepository.save(projectUser);
  }

  @Override
  public ProjectUser createProjectUserShare(ProjectUser projectUser) {
    log.info("(createProjectUserShare)user: {}, project: {}, role: {}", projectUser.getUserId(), projectUser.getProjectId(), projectUser.getRole());
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

  @Override
  @Transactional
  public String updateRoleProjectUser(String projectId, String memberId, String role) {
    log.info("(updateRoleProjectUser)project: {}, member: {}, role: {}", projectId, memberId, role);
    if(projectUserRepository.existByUserIdAndProjectId(memberId, projectId) == null){
      log.error("(updateRoleProjectUser)project: {}", projectId);
      throw new ProjectUserNotFoundException();
    }
    projectUserRepository.updateRoleByUserIdAndProjectId(projectId, memberId, role);
    return projectUserRepository.getRoleProjectUser(memberId, projectId);
  }

  @Override
  @Transactional
  public void deleteAllByProjectId(String projectId) {
    log.info("(deleteAllByProjectId)projectId: {}", projectId);
    projectUserRepository.deleteAllByProjectId(projectId);
  }

  @Override
  public void deleteByUserIdAndProjectId(String memberId, String projectId) {
    log.info("(deleteByUserIdAndProjectId)user: {}, project: {}", memberId, projectId);
    if(projectUserRepository.existByUserIdAndProjectId(memberId, projectId) == null){
      log.error("(deleteByUserIdAndProjectId)project: {}", projectId);
      throw new ProjectUserNotFoundException();
    }
    projectUserRepository.deleteByUserIdAndProjectId(memberId, projectId);
  }

  @Override
  public List<ProjectUser> getAll() {
    log.info("(getAll)");
    return projectUserRepository.findAll();
  }

  @Override
  public void deleteById(String id) {
    log.info("(deleteById)id: {}", id);
    projectUserRepository.deleteById(id);
  }
}
