package ch.documakery.domain.user.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import ch.documakery.validation.constraints.UniqueEmail;
import ch.documakery.validation.constraints.UniqueNickname;

/**
 * Object used to transfer user registration information from client to server.
 * This object is not persisted.
 * 
 * @author Marco Jakob
 */
public class UserRegisterDto {
  
  @NotBlank
  @Email
  @UniqueEmail
  private String email;
  
  @NotBlank
  @Size(min = 4, max = 25)
  @UniqueNickname
  private String nickname;
  
  @NotBlank
  @Size(min = 4, max = 25)
  private String password;
  
  /**
   * Default constructor.
   */
  public UserRegisterDto() {
  }
  
  /**
   * Constructor.
   * 
   * @param email
   * @param nickname
   * @param password
   */
  public UserRegisterDto(String email, String nickname, String password) {
    super();
    this.email = email;
    this.nickname = nickname;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public void setPassword(String password) {
    this.password = password;
  }
}  
