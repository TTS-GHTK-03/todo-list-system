package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ghtk.todo_list.validation.ValidateEmail;

@Data
public class VerifyEmailRequest {

  @NotBlank(message = "Email is required")
  @ValidateEmail
  private String email;
}
