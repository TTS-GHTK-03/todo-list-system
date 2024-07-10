package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.ghtk.todo_list.validation.ValidateEmail;
import org.ghtk.todo_list.validation.ValidatePassword;

@Getter
@Builder
public class ResetPasswordRequest {

    @NotBlank(message = "email is required")
    @ValidateEmail
    private String email;
    private String resetPasswordKey;
    @NotBlank(message = "password is required")
    @ValidatePassword
    private String password;
    @NotBlank(message = "confirmPassword is required")
    @ValidatePassword
    private String confirmPassword;
}
