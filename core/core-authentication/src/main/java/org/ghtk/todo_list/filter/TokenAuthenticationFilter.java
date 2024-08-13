package org.ghtk.todo_list.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.service.AuthAccountService;
import org.ghtk.todo_list.service.AuthTokenService;
import org.ghtk.todo_list.service.AuthUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final AuthTokenService authTokenService;
  private final AuthUserService authUserService;
  private final AuthAccountService authAccountService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("(doFilterInternal)request: {}, response: {}, filterChain: {}", request, response, filterChain);
    final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (Objects.isNull(accessToken)) {
      filterChain.doFilter(request, response);
      return;
    }

    if (!accessToken.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    var jwtToken = accessToken.substring(7);
    String userId;
    try {
      userId = authTokenService.getSubjectFromAccessToken(jwtToken);
    } catch (Exception ex) {
      log.error("(doFilterInternal)request: {}, response: {}, filterChain: {}", request, response, filterChain);
      filterChain.doFilter(request, response);
      return;
    }

    if(Objects.nonNull(userId) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
      var user = authUserService.findById(userId);
      var account = authAccountService.findByUserIdWithThrow(userId);
      if(authTokenService.validateAccessToken(jwtToken, userId)) {
        var usernamePasswordAuthToken =
            new UsernamePasswordAuthenticationToken(
                account.getUsername(), user.getId(), new ArrayList<>());
        usernamePasswordAuthToken.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
