package org.ghtk.todo_list.mapper.impl;

import java.util.List;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.mapper.ProjectInformationResponseMapper;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectInformationResponseMapperImpl implements ProjectInformationResponseMapper {

  @Override
  public ProjectInformationResponse toProjectInformationResponse(Project project,
      String roleProjectUser, List<UserNameResponse> userNameResponseList) {
    ProjectInformationResponse projectInformationResponse = new ProjectInformationResponse();
    projectInformationResponse.setId(project.getId());
    projectInformationResponse.setTitle(project.getTitle());
    projectInformationResponse.setKeyProject(project.getKeyProject());
    projectInformationResponse.setRoleUser(roleProjectUser);
    projectInformationResponse.setUserNameResponseList(userNameResponseList);
    return projectInformationResponse;
  }
}
