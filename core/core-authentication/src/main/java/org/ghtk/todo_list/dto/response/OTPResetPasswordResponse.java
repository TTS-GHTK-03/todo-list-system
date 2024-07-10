package org.ghtk.todo_list.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OTPResetPasswordResponse {

    private String resetPasswordKey;
}
