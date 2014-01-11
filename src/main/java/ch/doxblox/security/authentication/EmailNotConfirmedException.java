package ch.doxblox.security.authentication;

import org.springframework.security.authentication.AccountStatusException;

/**
 * Thrown if an authentication request is rejected because the users email has not been confirmed.
 * 
 * @author Marco Jakob
 */
public class EmailNotConfirmedException extends AccountStatusException {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs an <code>EmailNotConfirmedException</code> with the specified message.
   * 
   * @param msg the detail message.
   */
  public EmailNotConfirmedException(String msg) {
    super(msg);
  }

  /**
   * Constructs a <code>EmailNotConfirmedException</code> with the specified message and root cause.
   * 
   * @param msg the detail message.
   * @param t root cause
   */
  public EmailNotConfirmedException(String msg, Throwable t) {
    super(msg, t);
  }
}
