package org.ghtk.todo_list.service;

import io.jsonwebtoken.Claims;

public interface AuthTokenService {

  /* ACCESS TOKEN */
  String generateAccessToken(String userId, String email, String username);
  String getSubjectFromAccessToken(String accessToken);
  boolean validateAccessToken(String accessToken, String userId);

  /* REFRESH TOKEN */
  String generateRefreshToken(String userId, String email, String username);
  String getSubjectFromRefreshToken(String refreshToken);
  boolean validateRefreshToken(String refreshToken, String userId);


  /* SHARE TOKEN */
  String generateShareToken(String email, String role, String projectId, Long expireTime);
  String getSubjectFromShareToken(String shareToken);
  Claims getClaimsFromShareToken(String shareToken);
  boolean validateShareToken(String shareToken, String userId);

  long getAccessTokenLifeTime();
  long getRefreshTokenLifeTime();


}
