package org.ghtk.todo_list.core_exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private String code;
  private Long timestamp;
  private MessageResponse error;

  public static ErrorResponse of(String code, Long timestamp, MessageResponse error) {
    ErrorResponse wrapperResponse = new ErrorResponse();
    wrapperResponse.setCode(code);
    wrapperResponse.setTimestamp(timestamp);
    wrapperResponse.setError(error);
    return wrapperResponse;
  }
}
