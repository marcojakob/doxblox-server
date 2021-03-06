package ch.doxblox.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ch.doxblox.domain.user.User;

/**
 * Util to get the principal from the security context.
 * 
 * @author Marco Jakob
 */
@Component
public class SecurityContextUtil {
  private static final Logger LOG = LoggerFactory.getLogger(SecurityContextUtil.class);

  /**
   * Returns the currently authenticated principal.
   * 
   * @return the currently authenticated principal or null if no authentication information is
   *         available
   */
  public User getCurrentUser() {

    User principal = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object currentPrincipal = authentication.getPrincipal();
      if (currentPrincipal instanceof User) {
        principal = (User) currentPrincipal;
      }
    }

    LOG.debug("Getting current user from security context: {}", principal);
    return principal;
  }
}