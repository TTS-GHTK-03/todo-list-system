package org.ghtk.todo_list.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.exception.EmailNotSharedException;
import org.ghtk.todo_list.service.AuthUserService;
import org.ghtk.todo_list.service.ShareTokenService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class FilterShareToken extends OncePerRequestFilter {

  private final ShareTokenService shareTokenService;
  private final AuthUserService authUserService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    log.info("(doFilterInternal)request: {}, response: {}, filterChain: {}", request, response, filterChain);
    final String shareToken = request.getHeader("Share-Token");

    if (Objects.isNull(shareToken) || !shareToken.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    var jwtTokenShare = shareToken.substring(7);
    String email;
    try {
      email = shareTokenService.getSubjectFromShareToken(jwtTokenShare);
    } catch (Exception ex) {
      log.error("(doFilterInternal) Invalid shareToken - request: {}, response: {}, filterChain: {}", request, response, filterChain);
      filterChain.doFilter(request, response);
      return;
    }

    if (!shareTokenService.validateShareToken(jwtTokenShare, email)) {
      log.error("(doFilterInternal) Invalid shareToken - request: {}, response: {}, filterChain: {}", request, response, filterChain);
      filterChain.doFilter(request, response);
      return;
    }

    if(Objects.nonNull(email) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
      var user = authUserService.findByEmail(email);
      if(!email.equals(user.getEmail())) {
        log.error("(doFilterInternal)email: {} not shared", email);
        throw new EmailNotSharedException();
      }
    }

    filterChain.doFilter(request, response);
  }
}
