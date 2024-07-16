package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Project;

import java.util.List;

public interface ProjectService {

  List<Project> getAllProject(String userId);

  Project getProject(String userId, String projectId);

  Project getProjectInformation(String projectId);

  boolean existById(String id);

  Project createProject(String userId, String title);
  Project getProjectById(String projectId);
  void updateCountSprint(String projectId, Long countSprint);
}