package org.ghtk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.service.AuthTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

  private final String accessTokenJwtSecret;
  private final Long accessTokenLifeTime;

  private final String refreshTokenJwtSecret;
  private final Long refreshTokenLifeTime;
  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  @Transactional
  public String generateAccessToken(String userId, String email, String username, String role) {
    log.info("(generateAccessToken)userId: {}, email: {}, username: {}",
        userId, email, userId);
    var claims = new HashMap<String, Object>();
    claims.put("email", email);
    claims.put("username", username);
    claims.put("role", role);
    return generateToken(userId, claims, accessTokenLifeTime, accessTokenJwtSecret);
  }

  @Override
  @Transactional
  public String getSubjectFromAccessToken(String accessToken) {
    log.info("(getSubjectFromAccessToken)accessToken: {}", accessToken);
    return getClaim(accessToken, Claims::getSubject, accessTokenJwtSecret);
  }

  @Override
  @Transactional
  public boolean validateAccessToken(String accessToken, String userId) {
    log.debug("(validateAccessToken)accessToken: {}, userId: {}", accessToken, userId);
    if(!getSubjectFromAccessToken(accessToken).equals(userId) || redisTemplate.opsForValue().get("accessToken:" + userId) == null){
      return false;
    }
    return true;
  }

  @Override
  @Transactional
  public String generateRefreshToken(String userId, String email, String username, String role) {
    log.info("(generateRefreshToken)userId: {}, email: {}, username: {}",
        userId, email, userId);
    var claims = new HashMap<String, Object>();
    claims.put("email", email);
    claims.put("username", username);
    claims.put("role", role);
    return generateToken(userId, claims, refreshTokenLifeTime, refreshTokenJwtSecret);
  }

  @Override
  @Transactional
  public String getSubjectFromRefreshToken(String refreshToken) {
    log.info("(getSubjectFromRefreshToken)refreshToken: {}", refreshToken);
    return getClaim(refreshToken, Claims::getSubject, refreshTokenJwtSecret);

  }

  @Override
  @Transactional
  public boolean validateRefreshToken(String refreshToken, String userId) {
    log.debug("(validateRefreshToken)refreshToken: {}, userId: {}", refreshToken, userId);
    if(!getSubjectFromAccessToken(refreshToken).equals(userId) || redisTemplate.opsForValue().get("refreshToken:" + userId) == null){
      return false;
    }
    return true;
  }

  private Claims getClaims(String token, String secretKey) {
    log.info("(getClaims)token: {} secretKey: {}", token, secretKey);
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  private <T> T getClaim(String token, Function<Claims, T> claimsResolve, String secretKey) {
    log.info("(getClaim) token: {}, claimResolve: {}, secretKey: {}", token, claimsResolve, secretKey);
    return claimsResolve.apply(getClaims(token, secretKey));
  }

  private String generateToken(String subject, Map<String, Object> claims, long tokenLifeTime, String jwtSecret) {
    log.info("(generateToken)subject: {}, claims: {}, tokenLifeTime: {}, jwtSecret: {}", subject, claims, tokenLifeTime, jwtSecret);
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact();
  }
}
