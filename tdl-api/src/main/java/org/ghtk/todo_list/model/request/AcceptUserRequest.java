package org.ghtk.todo_list.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateEmail;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptUserRequest {

  @NotBlank(message = "Email is required")
  @ValidateEmail
  private String email;
}
