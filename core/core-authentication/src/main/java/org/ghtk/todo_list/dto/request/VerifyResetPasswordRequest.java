package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ghtk.todo_list.validation.ValidateEmail;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResetPasswordRequest {

    @NotBlank(message = "email is required")
    @ValidateEmail
    private String email;
    @NotBlank(message = "otp is required")
    private String otp;
}
