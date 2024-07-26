package org.ghtk.todo_list.facade;

import java.util.List;
import org.ghtk.todo_list.entity.AuthUser;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.ghtk.todo_list.model.response.ProjectRoleResponse;
import org.ghtk.todo_list.paging.PagingRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectFacadeService {

  List<ProjectInformationResponse> getAllProject(String userId);

  ProjectRoleResponse getProject(String userId, String projectId);

  ProjectInformationResponse getProjectInformation(String userId, String projectId);

  Project createProject(String userId, String title);

  Project updateProject(String userId, String projectId, String title, String keyProject);

  void deleteProject(String userId, String projectId);

  PagingRes<Project> searchProjects(String searchValue, Pageable pageable, String userId);

  List<AuthUser> searchUser(String searchValue, List<String> roles, String projectId, String userId);
}
