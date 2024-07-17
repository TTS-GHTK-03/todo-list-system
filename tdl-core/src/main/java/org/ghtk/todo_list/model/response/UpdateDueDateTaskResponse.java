package org.ghtk.todo_list.model.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDueDateTaskResponse {
  private String taskId;
  private String status;
  private LocalDate dueDate;
}
