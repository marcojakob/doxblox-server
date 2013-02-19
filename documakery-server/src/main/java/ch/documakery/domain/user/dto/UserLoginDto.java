package ch.documakery.domain.user.dto;

/**
 * Object used to transfer user login information from client to server.
 * This object is not persisted.
 * 
 * @author Marco Jakob
 */
public class UserLoginDto {
  
  private String email;
  private String password;
  private boolean rememberMe;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean getRememberMe() {
    return rememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    this.rememberMe = rememberMe;
  }
}
