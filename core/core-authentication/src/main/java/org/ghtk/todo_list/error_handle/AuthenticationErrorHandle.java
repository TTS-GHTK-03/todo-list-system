package org.ghtk.todo_list.error_handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationErrorHandle implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException
  ) throws IOException, ServletException {
    var error = new HashMap<String, Object>();
    error.put("status", 401);
    error.put("timestamp", LocalDateTime.now());
    error.put("message", "UnAuthenticated.");
    response.sendError(401, new ObjectMapper().writeValueAsString(error));
  }
}
