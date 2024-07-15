package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProject;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Board;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectTitleAlreadyExistedException;
import org.ghtk.todo_list.exception.ProjectUserNotFoundException;
import org.ghtk.todo_list.exception.RoleProjectNotFoundException;
import org.ghtk.todo_list.mapper.BoardMapper;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.ghtk.todo_list.mapper.ProjectUserMapper;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.ghtk.todo_list.repository.BoardRepository;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.repository.ProjectUserRepository;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.ProjectService;

import java.util.List;
import org.ghtk.todo_list.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectUserService projectUserService;
  private final BoardService boardService;
  private final AuthUserService authUserService;
  private final ProjectMapper projectMapper;
  private final ProjectInformationResponseMapper projectInformationResponseMapper;

  @Override
  public List<Project> getAllProject(String userId) {
    log.info("(getAllProject)userId: {}", userId);
    //check user exist

    //
    List<Project> projectList = projectRepository.getAllProject(userId);
    return projectList;
  }

  @Override
  public Project getProject(String userId, String projectId) {
    log.info("(getProject)user: {}", userId);
    //check user exist

    //
    if (!projectRepository.existsById(projectId)) {
      log.error("(getProject)user: {} not found ", projectId);
      throw new ProjectNotFoundException();
    }

    Project project = projectRepository.getProject(userId, projectId);

    if (project == null) {
      log.error("(getProject)user: {} user doesn't have project", projectId);
      throw new ProjectUserNotFoundException();
    }

    return project;
  }

  @Override
  public ProjectInformationResponse getProjectInformation(String userId, String projectId) {
    log.info("(getProjectInformation)project: {}", projectId);

    if (!projectRepository.existsById(projectId)) {
      log.error("(getProjectInformation)project: {}", projectId);
      throw new ProjectNotFoundException();
    }
    Project project = projectRepository.findById(projectId).get();
    String roleProjectUser = projectUserService.getRoleProjectUser(userId, projectId);
    List<UserNameResponse> userNameResponseList = authUserService.getNameUser(userId, projectId);

    return projectInformationResponseMapper.toProjectInformationResponse(project, roleProjectUser,
        userNameResponseList);
  }

  @Override
  public Project createProject(String userId, String title) {
    log.info("(createProject)user: {}", userId);

    if (projectRepository.findByTitle(title) != null) {
      log.error("(createProject)project: {} already has title ", title);
      throw new ProjectTitleAlreadyExistedException();
    }

    String regex = "[\\s,;:.!?-]+";
    String[] titleList = title.split(regex);
    StringBuilder stringBuilder = new StringBuilder();
    for (String tl : titleList) {
      stringBuilder.append(tl.toUpperCase().charAt(0));
    }

    int count = 1;
    String keyProjectCheck = stringBuilder.toString() + count;
    while (projectRepository.findByKeyProject(keyProjectCheck) != null) {
      count++;
      keyProjectCheck = stringBuilder.toString() + count;
    }

    Project project = projectMapper.toProject(title, stringBuilder.append(count).toString(),
        LocalDateTime.now(), LocalDateTime.now());
    Project projectSaved = projectRepository.save(project);

    ProjectUser projectUser = projectUserService.createProjectUser(userId, projectSaved.getId(),
        "ADMIN", LocalDateTime.now(), LocalDateTime.now());

    return projectSaved;
  }

  @Override
  public boolean existById(String id) {
    log.info("(existById)id: {}", id);
    return projectRepository.existsById(id);
  }

  @Override
  public Project getProjectById(String projectId) {
    log.info("(getProjectById)projectId: {}", projectId);
    return projectRepository.findById(projectId).orElseThrow(() -> {
      log.error("(getProjectById)projectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    });
  }

  @Override
  public void updateCountSprint(String projectId, Long countSprint) {
    log.info("(updateCountSprint)projectId: {}, countSprint: {}", projectId, countSprint);
    projectRepository.updateCountSprint(projectId, countSprint);
  }
}
