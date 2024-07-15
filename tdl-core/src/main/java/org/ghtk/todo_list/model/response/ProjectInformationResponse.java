package org.ghtk.todo_list.model.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.ghtk.todo_list.dto.response.UserNameResponse;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectInformationResponse implements Serializable {
  private String id;
  private String title;
  private String keyProject;
  private String roleUser;
  private List<UserNameResponse> userNameResponseList;
}
