package org.ghtk.todo_list.mapper;

import java.util.List;
import org.ghtk.todo_list.dto.response.UserNameResponse;
import org.ghtk.todo_list.entity.Project;
import org.ghtk.todo_list.model.response.ProjectInformationResponse;

public interface ProjectInformationResponseMapper {

  ProjectInformationResponse toProjectInformationResponse(Project project,
      String roleProjectUser, List<UserNameResponse> userNameResponseList);
}
