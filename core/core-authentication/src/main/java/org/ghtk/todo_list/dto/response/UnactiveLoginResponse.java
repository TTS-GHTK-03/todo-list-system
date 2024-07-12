package org.ghtk.todo_list.dto.response;

public class UnactiveLoginResponse extends LoginResponse {

  private String message;

  public UnactiveLoginResponse() {
  }

  public UnactiveLoginResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
