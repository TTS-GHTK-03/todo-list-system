package org.ghtk.todo_list.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.ghtk.todo_list.validation.ValidateEmail;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

    @NotBlank(message = "email is required")
    @ValidateEmail
    @Schema(description = "Email", example = "QpCqA@gmail.com")
    private String email;
}
