package org.ghtk.todo_list.dto.response;

import lombok.Data;

@Data
public class ActiveLoginResponse extends LoginResponse{

  private String accessToken;
  private String refreshToken;
  private long accessTokenLifeTime;
  private long refreshTokenLifeTime;
}
