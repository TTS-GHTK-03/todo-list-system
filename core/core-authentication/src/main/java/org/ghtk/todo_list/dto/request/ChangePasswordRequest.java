package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.ghtk.todo_list.validation.ValidatePassword;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

  @NotBlank(message = "old password is required")
  @ValidatePassword
  private String oldPassword;
  @NotBlank(message = "new password is required")
  @ValidatePassword
  private String newPassword;
  @NotBlank(message = "confirm password is required")
  @ValidatePassword
  private String confirmPassword;

}
