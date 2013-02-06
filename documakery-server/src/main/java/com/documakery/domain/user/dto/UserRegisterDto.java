package com.documakery.domain.user.dto;

/**
 * Object used to transfer user registration information from client to server.
 * This object is not persisted.
 * 
 * @author Marco Jakob
 */
public class UserRegisterDto {
  
  private String email;
  private String nickname;
  private String password;
  private String passwordConfirm;
  
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
  
  public String getPasswordConfirm() {
    return passwordConfirm;
  }
  
  public void setPasswordConfirm(String passwordConfirm) {
    this.passwordConfirm = passwordConfirm;
  }
  
}
