package org.ghtk.todo_list.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerifyRegisterResponse {

  private String registerKey;

}
