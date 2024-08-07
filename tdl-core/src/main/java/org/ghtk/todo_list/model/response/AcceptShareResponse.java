package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptShareResponse {

  private String email;
  private String shareToken;
  private String status;
  private String projectId;
}
