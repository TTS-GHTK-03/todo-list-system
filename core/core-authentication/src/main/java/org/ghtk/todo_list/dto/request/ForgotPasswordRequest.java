package org.ghtk.todo_list.dto.request;

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
    private String email;
}
