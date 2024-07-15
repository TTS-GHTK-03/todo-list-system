package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;

public interface ProjectFacadeService {

  List<Project> getAllProject(String userId);

  Project getProject(String userId, String projectId);

  ProjectInformationResponse getProjectInformation(String userId, String projectId);

  Project createProject(String userId, String title);
}
