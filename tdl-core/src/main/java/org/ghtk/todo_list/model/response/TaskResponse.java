package org.ghtk.todo_list.model.response;

import lombok.Data;

@Data
public class TaskResponse {

  private String id;
  private String title;

  public TaskResponse(String id, String title) {
    this.id = id;
    this.title = title;
  }
}
