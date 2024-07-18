package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TypeRequest {

  @NotBlank(message = "Title is required")
  private String title;

  @NotBlank(message = "Image is required")
  private String image;

  private String description;
}
