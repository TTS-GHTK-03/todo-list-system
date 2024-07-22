package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.ghtk.todo_list.entity.Task;
import org.ghtk.todo_list.repository.UserProjection;

@Data
@AllArgsConstructor(staticName = "of")
@Builder
public class TaskResponse {

  private String id;
  private String title;
  private Integer point;
  private String status;
  private String userId;


  public TaskResponse() {
  }

  public TaskResponse(String id, String title, Integer point, String status) {
    this.id = id;
    this.title = title;
    this.point = point;
    this.status = status;
  }

  public static TaskResponse from(Task task, String userId) {
    TaskResponse response = new TaskResponse();
    response.setId(task.getId());
    response.setTitle(task.getTitle());
    response.setPoint(task.getPoint());
    response.setStatus(task.getStatus());
    response.setUserId(userId);
    return response;
  }
}
