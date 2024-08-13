package org.ghtk.todo_list.model.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptInviteResponse{

  private String email;
  private String status;
  private String projectId;
}
