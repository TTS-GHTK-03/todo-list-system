package org.ghtk.todo_list.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {

  private String id;
  private String title;

}
