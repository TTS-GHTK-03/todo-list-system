package org.ghtk.todo_list.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

  public static String getUserId() {
    log.info("(getAccountId)");
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      return "SYSTEM_ID";
    }
    return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
  }
}
