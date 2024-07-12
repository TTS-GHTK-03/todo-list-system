package org.ghtk.todo_list.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.exception.ProjectNotFoundException;
import org.ghtk.todo_list.exception.ProjectTitleAlreadyExistedException;
import org.ghtk.todo_list.repository.ProjectRepository;
import org.ghtk.todo_list.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;

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
    if (!projectRepository.existsById(projectId) ) {
      log.error("(getProject)user: {} not found ", projectId);
      throw new ProjectNotFoundException();
    }
    Project project = projectRepository.getProject(userId, projectId);
    return project;
  }

  @Override
  public Project createProject(String userId, String title, String role) {
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

    Project project = new Project();
    project.setTitle(title);
    project.setRole(role);
    project.setKey(stringBuilder.toString());
    project.setCreatedAt(LocalDateTime.now());
    project.setLastUpdatedAt(LocalDateTime.now());
    System.out.println(project);

//    return null;
    return projectRepository.save(project);
  }
}
