package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ProjectRoleResponse {

  private String id;
  private String title;
  private String keyProject;
  private String roleUser;
}
