package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.ghtk.todo_list.model.response.ProjectRoleResponse;

public interface ProjectFacadeService {

  List<Project> getAllProject(String userId);

  ProjectRoleResponse getProject(String userId, String projectId);

  ProjectInformationResponse getProjectInformation(String userId, String projectId);

  Project createProject(String userId, String title);

  Project updateProject(String userId, String projectId, String title, String keyProject);

  void deleteProject(String userId, String projectId);
}
