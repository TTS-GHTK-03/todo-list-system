package org.ghtk.todo_list.service;

import org.ghtk.todo_list.entity.Project;

import java.util.List;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;

public interface ProjectService {

  List<Project> getAllProject(String userId);

  Project getProject(String userId, String projectId);

  Project getProjectInformation(String projectId);

  boolean existById(String id);

  Project createProject(String userId, String title);
}