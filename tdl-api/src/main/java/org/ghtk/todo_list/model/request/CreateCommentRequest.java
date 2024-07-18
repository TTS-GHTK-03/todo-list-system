package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

  @NotBlank(message = "Text is required")
  private String text;
}

