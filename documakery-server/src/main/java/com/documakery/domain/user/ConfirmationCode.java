package com.documakery.domain.user;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Confirmation code used to confirm email addresses.
 *
 * @author Marco Jakob
 */
public class ConfirmationCode {

  /**
   * The confirmation code. 
   */
  @Indexed(unique=true)
  private String code;
  
  /**
   * The expiration date of this confirmation code.
   */
  private Date expirationDate;

  /**
   * Creates a confirmation code.
   * 
   * @param code The confirmation code associated with the user.
   * @param expirationDate Expiration date for the confirmation code.
   */
  public ConfirmationCode(String code, Date expirationDate) {
    this.code = code;
    this.expirationDate = expirationDate;
  }

  public String getCode() {
    return code;
  }

  public Date getExpirationDate() {
    return expirationDate;
  }
}
