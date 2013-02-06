package com.documakery.domain.user.dto;

public class UserDto {
  
  private String email;
  private String nickname;
  
  public UserDto(String email, String nickname) {
    this.email = email;
    this.nickname = nickname;
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
}
