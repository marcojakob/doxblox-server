package com.documakery.domain.user.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.documakery.validation.constraints.UniqueEmail;
import com.documakery.validation.constraints.UniqueNickname;

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
