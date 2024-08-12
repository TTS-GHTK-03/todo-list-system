package org.ghtk.todo_list.service.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.entity.ProjectUser;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectTitleAlreadyExistedException;
import org.ghtk.todo_list.exception.ProjectUserNotFoundException;
import org.ghtk.todo_list.filter.FilterProject;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.mapper.ProjectMapper;
import org.ghtk.todo_list.paging.PagingRes;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.BoardService;
import org.ghtk.todo_list.service.ProjectService;

import java.util.List;
import org.ghtk.todo_list.service.ProjectUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;

  @Override
  public List<Project> getAllProject(String userId) {
    log.info("(getAllProject)userId: {}", userId);
    List<Project> projectList = projectRepository.getAllProject(userId);
    return projectList;
  }

  @Override
  public Project getProject(String userId, String projectId) {
    log.info("(getProject)user: {}, project: {}", userId, projectId);

    validateProjectId(projectId);

    Project project = projectRepository.getProject(userId, projectId);

    if (project == null){
      log.error("(getProject)user: {} doesn't have project {}", userId, projectId);
      throw new ProjectUserNotFoundException();
    }

    return project;
  }

  @Override
  public Project getProjectInformation(String projectId) {
    log.info("(getProjectInformation)project: {}", projectId);

    validateProjectId(projectId);
    return projectRepository.findById(projectId).get();
  }

  @Override
  public Project createProject(String userId, String title) {
    log.info("(createProject)user: {}", userId);

    validateTitle(title);

    String regex = "[\\s,;:.!?-]+";
    String[] titleList = title.split(regex);
    StringBuilder stringBuilder = new StringBuilder();
    for (String tl : titleList) {
      stringBuilder.append(tl.toUpperCase().charAt(0));
    }

    int count = 1;
    String keyProjectCheck = stringBuilder.toString() + count;

    while (projectRepository.existsByKeyProject(keyProjectCheck)){
      count++;
      keyProjectCheck = stringBuilder.toString() + count;
    }

    Project project = projectMapper.toProject(title, stringBuilder.append(count).toString());
    return projectRepository.save(project);
  }

  @Override
  public Project updateProject(Project project) {
    log.info("(updateProject)project: {}", project);
    return projectRepository.save(project);
  }


  @Override
  public boolean existById(String id) {
    log.info("(existById)id: {}", id);
    return projectRepository.existsById(id);
  }

  @Override
  public boolean existByTitle(String title) {
    log.info("(existByTitle)title: {}", title);
    return projectRepository.existsByTitle(title);
  }

  @Override
  public boolean existByKeyProject(String keyProject) {
    log.info("(existByKeyProject)keyProject: {}", keyProject);
    return projectRepository.existsByKeyProject(keyProject);
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
  @Transactional
  public void deleteProject(String projectId) {
    log.info("(deleteProject)projectId: {}", projectId);
    projectRepository.deleteById(projectId);
  }

  private void validateProjectId(String projectId){
    log.info("(validateProjectId)projectId: {}", projectId);
    if(!projectRepository.existsById(projectId)){
      log.error("(validateProjectId)project: {} not found", projectId);
      throw new ProjectNotFoundException();
    }
  }

   private void validateTitle(String title){
    log.info("(validateTitle)title: {}", title);
     if (projectRepository.existsByTitle(title)) {
       log.error("(validateTitle)project: {} already existed ", title);
       throw new ProjectTitleAlreadyExistedException();
     }
   }

  @Override
  @Transactional
  public PagingRes<Project> searchProjects(String searchValue, Pageable pageable, String userId) {
    log.info("(searchProjects)searchValue: {}", searchValue);
    return new PagingRes<>(projectRepository.findAll(FilterProject.getProjectsByCriteria(searchValue, userId), pageable));
  }

  @Override
  public String findTitleProjectById(String projectId) {
    log.info("(findTitleProjectById)projectId: {}", projectId);
    return projectRepository.findTitleProjectById(projectId);
  }
}
