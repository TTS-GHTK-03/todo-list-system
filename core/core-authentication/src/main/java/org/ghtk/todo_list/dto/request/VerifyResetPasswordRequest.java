package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Email", example = "QpCqA@gmail.com")
    private String email;
    @NotBlank(message = "otp is required")
    @Schema(description = "Otp", example = "123456")
    private String otp;
}
