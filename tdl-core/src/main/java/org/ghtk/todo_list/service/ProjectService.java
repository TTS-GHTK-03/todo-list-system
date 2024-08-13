package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Project;

import java.util.List;
import org.ghtk.todo_list.paging.PagingRes;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

  List<Project> getAllProject(String userId);

  Project getProject(String userId, String projectId);

  Project getProjectInformation(String projectId);

  boolean existById(String id);
  boolean existByTitle(String title);
  boolean existByKeyProject(String keyProject);

  Project createProject(String userId, String title);
  Project updateProject(Project project);
  Project getProjectById(String projectId);
  void deleteProject(String projectId);
  PagingRes<Project> searchProjects(String searchValue, Pageable pageable, String userId);
  String findTitleProjectById(String projectId);
}