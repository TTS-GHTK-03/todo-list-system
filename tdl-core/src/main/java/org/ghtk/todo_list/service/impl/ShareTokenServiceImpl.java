package org.ghtk.todo_list.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.service.ShareTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShareTokenServiceImpl implements ShareTokenService {

  @Value("${application.authentication.share_token.jwt_secret:otitraiwjga}")
  private String shareTokenJwtSecret;

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
  public String generateShareToken(String email, String role, String projectId, long expireTime) {
    log.info("(generateShareToken)email: {}, role: {}, projectId: {}, expireTime: {}", email, role, projectId, expireTime);
    var claims = new HashMap<String, Object>();
    claims.put("projectId", projectId);
    claims.put("email", email);
    claims.put("role", role);
    claims.put("expireTime", expireTime);
    return generateToken(email, claims, expireTime, shareTokenJwtSecret);
  }

  @Override
  public String getSubjectFromShareToken(String shareToken) {
    log.debug("(getSubjectFromShareToken)shareToken: {}", shareToken);
    return getClaim(shareToken, Claims::getSubject, shareTokenJwtSecret);
  }

  @Override
  public Claims getClaimsFromShareToken(String shareToken) {
    log.debug("(getClaimsFromShareToken)shareToken: {}", shareToken);
    return getClaims(shareToken, shareTokenJwtSecret);
  }

  @Override
  public boolean validateShareToken(String shareToken, String email) {
    log.info("(validateShareToken)shareToken: {}, projectId: {}", shareToken, email);
    return getSubjectFromShareToken(shareToken).equals(email) && isActiveToken(shareToken, shareTokenJwtSecret);
  }
}
