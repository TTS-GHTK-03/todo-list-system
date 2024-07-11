package org.ghtk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class AuthTokenServiceImpl implements AuthTokenService {

  @Value("${application.authentication.access_token.jwt_secret:asfasfasfafasfafasf}")
  private String accessTokenSecretKey;

  @Value("${application.authentication.access_token.life_time:435346}")
  private Long accessTokenLifeTime;

  @Value("${application.authentication.refresh_token.jwt_secret:twetdhgfjfgjfgjfjt}")
  private String refreshTokenSecretKey;

  @Value("${application.authentication.refresh_token.life_time:568457}")
  private Long refreshTokenLifeTime;

  @Override
  public String getSubjectFromAccessToken(String accessToken) {
    log.info("(getSubjectFromAccessToken)accessToken : {}", accessToken);
    return getClaim(accessToken, Claims::getSubject, accessTokenSecretKey);
  }

  @Override
  public String getSubjectFromRefreshToken(String refreshToken) {
    log.info("(getSubjectFromAccessToken)refreshToken : {}", refreshToken);
    return getClaim(refreshToken, Claims::getSubject, refreshTokenSecretKey);
  }

  @Override
  public boolean isExpiredAccessToken(String token) {
    return isExpiredToken(token, accessTokenSecretKey);
  }

  @Override
  public boolean isExpiredRefreshToken(String token) {
    return isExpiredToken(token, refreshTokenSecretKey);
  }

  @Override
  public String generateAccessToken(String username, String email) {
    log.info("(generateAccessToken) username: {}, email: {}", email, username);
    var claims = new HashMap<String, Object>();
    claims.put("username", username);
    claims.put("email", email);
    return generateToken(username, claims, accessTokenLifeTime, accessTokenSecretKey);
  }

  @Override
  public String generateRefreshToken(String username, String email) {
    log.info("(generateRefreshToken) username: {}, email: {}", email, username);
    var claims = new HashMap<String, Object>();
    claims.put("username", username);
    claims.put("email", email);
    return generateToken(username, claims, refreshTokenLifeTime, refreshTokenSecretKey);
  }

  @Override
  public String convertWildcardRequestUri(String uri) {
    String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    String numericPattern = "/\\d+";
    return removeQueryParams(uri.replaceAll(uuidPattern, "*").replaceAll(numericPattern, "/*"));
  }

  private String generateToken(
      String subject, Map<String, Object> claims, long tokenLifeTime, String jwtSecret) {
    log.info(
        "(generateToken)subject : {}, claims : {}, tokenLifeTime : {}, jwtSecret : {}",
        subject,
        claims,
        tokenLifeTime,
        jwtSecret);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();
  }

  @Override
  public boolean validateAccessToken(String accessToken, String userId) {
    log.info("(validateAccessToken)accessToken: {}, userId: {}", accessToken, userId);
    return getSubjectFromAccessToken(accessToken).equals(userId) && !isExpiredToken(accessToken, accessTokenSecretKey);
  }

  private <T> T getClaim(String token, Function<Claims, T> claimsResolve, String secretKey) {
    log.info("(getClaim)token : {}, claimsResolve : {}, secretKey : {}", token, claimsResolve,
        secretKey);
    return claimsResolve.apply(getClaims(token, secretKey));
  }

  private Claims getClaims(String token, String secretKey) {
    log.info("(getClaims)token : {}, secretKey : {}", token, secretKey);
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  private boolean isExpiredToken(String token, String secretKey) {
    return getClaim(token, Claims::getExpiration, secretKey).before(new Date());
  }

  private String removeQueryParams(String uri) {
    if (uri.contains("?")) {
      uri = uri.split("\\?")[0];
    }
    return uri;
  }
}
