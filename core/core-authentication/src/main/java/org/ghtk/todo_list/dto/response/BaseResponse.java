package org.ghtk.todo_list.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@Data
@NoArgsConstructor
public class BaseResponse<T> {

  private int status;
  private String timestamp;
  private T data;

  public BaseResponse of(int status, T data) {
    return BaseResponse.of(status, String.valueOf(LocalDateTime.now()), data);
  }

  public static BaseResponse of(int status) {
    return BaseResponse.of(status, String.valueOf(LocalDateTime.now()), null);
  }
}
