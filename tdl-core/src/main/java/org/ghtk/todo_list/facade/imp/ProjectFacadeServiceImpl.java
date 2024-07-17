package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.ProjectService;
import org.ghtk.todo_list.service.ProjectUserService;

@Slf4j
@RequiredArgsConstructor
public class ProjectFacadeServiceImpl implements ProjectFacadeService {

  private final ProjectService projectService;
  private final ProjectUserService projectUserService;
  private final BoardService boardService;
  private final AuthUserService authUserService;
  private final ProjectInformationResponseMapper projectInformationResponseMapper;

  @Override
  public List<Project> getAllProject(String userId) {
    log.info("(getAllProject)userId: {}", userId);

    if(!authUserService.existById(userId)){
      log.error("(getAllProject)userId: {} not found", userId);
      throw new UserNotFoundException();
    }

    return projectService.getAllProject(userId);
  }

  @Override
  public Project getProject(String userId, String projectId) {
    log.info("(getProject)user: {}, project: {}", userId, projectId);

    if(!authUserService.existById(userId)){
      log.error("(getProject)userId: {} not found", userId);
      throw new UserNotFoundException();
    }

    return projectService.getProject(userId, projectId);
  }

  @Override
  public ProjectInformationResponse getProjectInformation(String userId, String projectId) {
    log.info("(getProjectInformation)user: {}, project: {}", userId, projectId);

    if(!authUserService.existById(userId)){
      log.error("(getProjectInformation)userId: {} not found", userId);
      throw new UserNotFoundException();
    }

    Project project = projectService.getProjectInformation(projectId);
    String roleProjectUser = projectUserService.getRoleProjectUser(userId, projectId);
    List<UserNameResponse> userNameResponseList = authUserService.getNameUser(projectId);

    return projectInformationResponseMapper.toProjectInformationResponse(project, roleProjectUser, userNameResponseList);
  }

  @Override
  public Project createProject(String userId, String title) {
    log.info("(createProject)user: {}", userId);

    if(!authUserService.existById(userId)){
      log.error("(createProject)userId: {} not found", userId);
      throw new UserNotFoundException();
    }

    Project projectSaved = projectService.createProject(userId, title);

    ProjectUser projectUser = projectUserService.createProjectUser(userId, projectSaved.getId(),
        RoleProjectUser.ADMIN.toString());

    return projectSaved;
  }
}
