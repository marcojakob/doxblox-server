package ch.documakery.domain.user.dto;

import org.springframework.security.core.userdetails.UserDetails;

import ch.documakery.domain.user.User;

public class UserDto {
  
  private String email;
  private String nickname;
  
  /**
   * Constructor.
   * 
   * @param email
   * @param nickname
   */
  public UserDto(String email, String nickname) {
    this.email = email;
    this.nickname = nickname;
  }
  
  /**
   * Constructor for converting {@link UserDetails} to dto.
   * 
   * @param principal
   */
  public UserDto(UserDetails principal) {
    this(principal.getUsername(), null);
  }
  
  /**
   * Constructor for converting {@link User} to dto.
   * 
   * @param principal
   */
  public UserDto(User user) {
    this(user.getEmail(), user.getNickname());
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
  
  @Override
  public String toString() {
    return email + " (" + nickname + ")";
  }

}
