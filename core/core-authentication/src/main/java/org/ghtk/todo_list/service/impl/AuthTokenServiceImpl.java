package org.ghtk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthTokenServiceImpl implements AuthTokenService {

  @Value("${application.authentication.access_token.jwt_secret:asfasfasfafasfafasf}")
  private String accessTokenJwtSecret;

  @Value("${application.authentication.access_token.life_time:435346}")
  private Long accessTokenLifeTime;

  @Value("${application.authentication.refresh_token.jwt_secret:twetdhgfjfgjfgjfjt}")
  private String refreshTokenJwtSecret;

  @Value("${application.authentication.refresh_token.life_time:568457}")
  private Long refreshTokenLifeTime;

  private String generateToken(String subject, Map<String, Object> claims, long tokenLifeTime, String jwtSecret) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();
  }

  private boolean isActiveToken(String token, String secretKey) {
    return getClaim(token, Claims::getExpiration, secretKey).after(new Date());
  }

  private <T> T getClaim(String token, Function<Claims, T> claimsResolve, String secretKey) {
    return claimsResolve.apply(getClaims(token, secretKey));
  }

  private Claims getClaims(String token, String secretKey) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  @Override
  public String generateAccessToken(String userId, String email, String username) {
    log.info("(generateAccessToken)userId: {}, email: {}, username: {}", userId, email, username);
    var claims = new HashMap<String, Object>();
    claims.put("email", email);
    claims.put("username", username);
    return generateToken(userId, claims, accessTokenLifeTime, accessTokenJwtSecret);
  }

  @Override
  public String getSubjectFromAccessToken(String accessToken) {
    log.debug("(getSubjectFromAccessToken)accessToken: {}", accessToken);
    return getClaim(accessToken, Claims::getSubject, accessTokenJwtSecret);
  }

  @Override
  public boolean validateAccessToken(String accessToken, String userId) {
    log.info("(validateAccessToken)accessToken: {}, userId: {}", accessToken, userId);
    return getSubjectFromAccessToken(accessToken).equals(userId) && isActiveToken(accessToken, accessTokenJwtSecret);
  }

  @Override
  public String generateRefreshToken(String userId, String email, String username) {
    log.info("(generateRefreshToken)userId: {}, email: {}, username: {}", userId, email, username);
    var claims = new HashMap<String, Object>();
    claims.put("email", email);
    claims.put("username", username);
    return generateToken(userId, claims, refreshTokenLifeTime, refreshTokenJwtSecret);
  }

  @Override
  public String getSubjectFromRefreshToken(String refreshToken) {
    log.debug("(getSubjectFromRefreshToken)refreshToken: {}", refreshToken);
    return getClaim(refreshToken, Claims::getSubject, refreshTokenJwtSecret);
  }

  @Override
  public boolean validateRefreshToken(String refreshToken, String userId) {
    log.info("(validateRefreshToken)refreshToken: {}, userId: {}", refreshToken, userId);
    return getSubjectFromRefreshToken(refreshToken).equals(userId) && isActiveToken(refreshToken, refreshTokenJwtSecret);
  }

  @Override
  public long getAccessTokenLifeTime() {
    return this.accessTokenLifeTime;
  }

  @Override
  public long getRefreshTokenLifeTime() {
    return this.refreshTokenLifeTime;
  }
}
