package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabelRequest {

  @NotBlank(message = "Title is required")
  private String title;

  private String description;

}
