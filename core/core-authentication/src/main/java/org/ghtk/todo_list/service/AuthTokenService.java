package org.ghtk.todo_list.service;

public interface AuthTokenService {

  String generateAccessToken(String userId, String email, String username, String role);
  String getSubjectFromAccessToken(String accessToken);
  boolean validateAccessToken(String accessToken, String userId);
  String generateRefreshToken(String userId, String email, String username, String role);
  String getSubjectFromRefreshToken(String refreshToken);
  boolean validateRefreshToken(String refreshToken, String userId);

}
