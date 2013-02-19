package ch.documakery.domain.user;

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
   * <code>true</code> if the email address was confirmed.
   */
  private boolean emailConfirmed;

  /**
   * The nickname.
   */
  @Indexed(unique=true)
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

  public boolean isEmailConfirmed() {
    return emailConfirmed;
  }

  public void setEmailConfirmed(boolean emailConfirmed) {
    this.emailConfirmed = emailConfirmed;
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
}