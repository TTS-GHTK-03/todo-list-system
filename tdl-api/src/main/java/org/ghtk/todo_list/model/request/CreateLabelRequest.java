package org.ghtk.todo_list.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabelRequest {

  @NotBlank(message = "Title is required")
  @Schema(description = "Label title", example = "stream login")
  private String title;

  @Schema(description = "Label description", example = "stream login description")
  private String description;

}
