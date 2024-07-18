package org.ghtk.todo_list.facade.imp;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.RoleProjectUser;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.ProjectKeyAlreadyExistedException;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectTitleAlreadyExistedException;
import org.ghtk.todo_list.exception.UserNotFoundException;
import org.ghtk.todo_list.facade.ProjectFacadeService;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
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
  private final ProjectMapper projectMapper;

  @Override
  public List<Project> getAllProject(String userId) {
    log.info("(getAllProject)userId: {}", userId);

    validateUserId(userId);

    return projectService.getAllProject(userId);
  }

  @Override
  public Project getProject(String userId, String projectId) {
    log.info("(getProject)user: {}, project: {}", userId, projectId);

    validateUserId(userId);

    return projectService.getProject(userId, projectId);
  }

  @Override
  public ProjectInformationResponse getProjectInformation(String userId, String projectId) {
    log.info("(getProjectInformation)user: {}, project: {}", userId, projectId);

    validateUserId(userId);

    Project project = projectService.getProjectInformation(projectId);
    String roleProjectUser = projectUserService.getRoleProjectUser(userId, projectId);
    List<UserNameResponse> userNameResponseList = authUserService.getNameUser(projectId);

    return projectInformationResponseMapper.toProjectInformationResponse(project, roleProjectUser, userNameResponseList);
  }

  @Override
  public Project createProject(String userId, String title) {
    log.info("(createProject)user: {}", userId);

    validateUserId(userId);

    Project projectSaved = projectService.createProject(userId, title);
    var user = authUserService.create("Unassigned");
    ProjectUser projectUser = projectUserService.createProjectUser(userId, projectSaved.getId(),
        RoleProjectUser.ADMIN.toString());
    projectUserService.createProjectUser(user.getId(), projectSaved.getId(), RoleProjectUser.VIEWER.toString());
    return projectSaved;
  }

  @Override
  public Project updateProject(String userId, String projectId, String title, String keyProject) {
    log.info("(updateProject)userId: {}, projectId: {}, title: {}, keyProject: {}", userId, projectId, title, keyProject);

    validateUserId(userId);
    validateProjectId(projectId);
    validateTitle(title);
    validateKeyProject(keyProject);

    Project project = projectMapper.toProject(title, keyProject.toUpperCase());
    project.setId(projectId);
    return projectService.updateProject(project);
  }

  private void validateUserId(String userId){
    log.info("(validateUserId)userId: {}", userId);
    if(!authUserService.existById(userId)){
      log.error("(validateUserId)userId: {} not found", userId);
      throw new UserNotFoundException();
    }
  }

  private void validateProjectId(String projectId){
    log.info("(validateProjectId)projectId: {}", projectId);
    if(!projectService.existById(projectId)){
      log.error("(validateProjectId)projectId: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

  private void validateTitle(String title){
    log.info("(validateTitle)title: {}", title);
    if(projectService.existByTitle(title)){
      log.error("(validateTitle)title: {} already existed", title);
      throw new ProjectTitleAlreadyExistedException();
    }
  }

  private void validateKeyProject(String keyProject){
    log.info("(validateKeyProject)keyProject: {}", keyProject);
    if(projectService.existByKeyProject(keyProject)){
      log.error("(validateKeyProject)keyProject: {} already existed", keyProject);
      throw new ProjectKeyAlreadyExistedException();
    }
  }
}
