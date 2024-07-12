package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.ghtk.todo_list.validation.ValidatePassword;

@Data
public class LoginRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 5, max = 20, message = "username must be between 8 and 20 character")
  private String username;
  @NotBlank(message = "Password is required")
  @ValidatePassword
  private String password;
}
