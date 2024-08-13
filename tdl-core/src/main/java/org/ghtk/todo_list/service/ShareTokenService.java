package org.ghtk.todo_list.service;

import io.jsonwebtoken.Claims;

public interface ShareTokenService {

  String generateShareToken(String email, String role, String projectId, long expireTime);
  String getSubjectFromShareToken(String shareToken);
  Claims getClaimsFromShareToken(String shareToken);
  boolean validateShareToken(String shareToken, String email);
}
