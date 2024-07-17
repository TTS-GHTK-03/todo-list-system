package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ghtk.todo_list.validation.ValidateLocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDueDateTaskRequest {

  @NotBlank(message = "Status task key is required")
  private String statusTaskKey;

  @NotBlank(message = "Due date is required")
  @ValidateLocalDate
  private String dueDate;
}
