package org.ghtk.todo_list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.ghtk.todo_list.validation.ValidateEmail;

@Getter
@Builder
public class OTPResetPasswordRequest {

    @NotBlank(message = "email is required")
    @ValidateEmail
    private String email;
    @NotBlank(message = "otp is required")
    private String otp;
}
