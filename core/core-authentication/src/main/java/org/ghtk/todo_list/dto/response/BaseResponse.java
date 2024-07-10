package org.ghtk.todo_list.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@Data
@NoArgsConstructor
public class BaseResponse {

  private int status;
  private String timestamp;
  private Object data;

  public static BaseResponse of(int status, Object data) {
    return BaseResponse.of(status, String.valueOf(LocalDateTime.now()), data);
  }

  public static BaseResponse of(int status) {
    return BaseResponse.of(status, String.valueOf(LocalDateTime.now()), null);
  }
}
