package com.documakery.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Util to get the principal from the security context.
 * 
 * @author Marco Jakob
 */
@Component
public class SecurityContextUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextUtil.class);

  /**
   * Returns the currently authenticated principal.
   * 
   * @return the currently authenticated principal or null if no authentication information is
   *         available
   */
  public UserDetails getPrincipal() {
    LOGGER.debug("Getting principal from the security context");

    UserDetails principal = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object currentPrincipal = authentication.getPrincipal();
      if (currentPrincipal instanceof UserDetails) {
        principal = (UserDetails) currentPrincipal;
      }
    }

    return principal;
  }
}