package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.ghtk.todo_list.repository.UserProjection;

@Data
@AllArgsConstructor
public class TaskResponse {

  private String id;
  private String title;
  private Integer point;
  private String status;
  private UserProjection userProjection;

  public TaskResponse(String id, String title, Integer point, String status) {
    this.id = id;
    this.title = title;
    this.point = point;
    this.status = status;
  }
}
