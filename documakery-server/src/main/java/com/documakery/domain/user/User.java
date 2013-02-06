package com.documakery.domain.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * The domain object for user information.
 * 
 * @author Marco Jakob
 */
@Document
public class User {
  
  @Id
  private ObjectId id;

  /**
   * The email address. Must be unique!
   */
  @Indexed(unique=true)
  private String email; 
  
  /**
   * The confirmation code. May be <code>null</code>.
   */
  private ConfirmationCode emailConfirmationCode;

  /**
   * The status of the account.
   */
  private AccountStatus accountStatus = AccountStatus.NEW;

  /**
   * The nickname.
   */
  private String nickname;

  /**
   * The hashed password.
   */
  private String password;

  /**
   * Create a new user with specified email.
   * 
   * @param email The new users email.
   */
  public User(String email) {
    this.email = email;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public ConfirmationCode getEmailConfirmationCode() {
    return emailConfirmationCode;
  }

  public void setEmailConfirmationCode(ConfirmationCode emailConfirmationCode) {
    this.emailConfirmationCode = emailConfirmationCode;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String hash) {
    this.password = hash;
  }

  /**
   * The status of the user's account.
   */
  public enum AccountStatus {
    /**
     * First status: User has been newly created. The new user will only have an email, i.e. NO
     * password, NO nickname, etc. This status should not be permanent. As soon as a registration
     * request returns, the Account should be in another status. If not, the user will have to
     * register again.
     */
    NEW,
  
    /**
     * Second status: E-mail has not been confirmed yet. The user will already have a password,
     * nickname, etc.
     */
    EMAIL_UNCONFIRMED,
  
    /**
     * The normal status. Email has been confirmed.
     */
    ENABLED,
  
    /**
     * The account has been disabled.
     */
    DISABLED,
  
    /**
     * The account has explicitly been locked.
     */
    LOCKED
  }
}