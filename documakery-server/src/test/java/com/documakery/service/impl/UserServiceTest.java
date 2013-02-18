package com.documakery.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.documakery.domain.user.SecurityRole;
import com.documakery.domain.user.User;
import com.documakery.domain.user.dto.UserRegisterDto;
import com.documakery.repository.UserRepository;
import com.documakery.security.util.SecurityContextUtil;

/**
 * Test for {@link UserServiceImpl}.
 * 
 * @author Marco Jakob
 */
public class UserServiceTest {

  private static final GrantedAuthority AUTHORITY = new SimpleGrantedAuthority(
      SecurityRole.ROLE_USER.name());
  private static final String EMAIL = "user@user.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";

  // SUT //
  private UserServiceImpl userService;

  private UserRepository userRepositoryMock;
  private SecurityContextUtil securityContextUtilMock;
  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Before
  public void setUp() {
    userRepositoryMock = mock(UserRepository.class);
    securityContextUtilMock = mock(SecurityContextUtil.class);
    
    userService = new UserServiceImpl(userRepositoryMock, securityContextUtilMock, passwordEncoder);
  }
  
  @Test
  public void getPrincipal_UserIsLoggedIn_ReturnPrincipal() {
    // given
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(AUTHORITY);
    
    UserDetails principal = new org.springframework.security.core.userdetails.User(
        EMAIL, PASSWORD, authorities);
    given(securityContextUtilMock.getPrincipal()).willReturn(principal);

    // when
    UserDetails loggedInPrincipal = userService.getPrincipal();
    
    // then
    verify(securityContextUtilMock, times(1)).getPrincipal();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    assertThat(loggedInPrincipal.getUsername(), is(EMAIL));
  }

  @Test
  public void getUser_UserIsLoggedIn_ReturnsUser() {
    // given
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(AUTHORITY);
    
    UserDetails principal = new org.springframework.security.core.userdetails.User(
        EMAIL, PASSWORD, authorities);
    given(securityContextUtilMock.getPrincipal()).willReturn(principal);

    User user = new User(EMAIL);
    given(userRepositoryMock.findByEmail(EMAIL)).willReturn(user);
    
    // when
    User loggedInUser = userService.getUser();

    // then
    verify(securityContextUtilMock, times(1)).getPrincipal();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    verify(userRepositoryMock, times(1)).findByEmail(anyString());
    verifyNoMoreInteractions(userRepositoryMock);
    
    assertThat(loggedInUser.getEmail(), is(EMAIL));
  }

  @Test
  public void getUser_UserIsNotLoggedIn_ReturnsNull() {
    // given
    given(securityContextUtilMock.getPrincipal()).willReturn(null);

    // when
    User loggedInUser = userService.getUser();

    // then
    verify(securityContextUtilMock, times(1)).getPrincipal();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    verifyZeroInteractions(userRepositoryMock);

    assertThat(loggedInUser, is(nullValue()));
  }

  @Test
  public void registerUser() {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(EMAIL);
    userRegister.setNickname(NICKNAME);
    userRegister.setPassword(PASSWORD);

    // when
    userService.register(userRegister);

    // then
    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    verify(userRepositoryMock, times(1)).save(userArgument.capture());
    verifyNoMoreInteractions(userRepositoryMock);
    
    assertThat(userArgument.getValue().isEmailConfirmed(), is(false));
    assertThat(userArgument.getValue().getEmail(), is(EMAIL));
    assertThat(userArgument.getValue().getNickname(), is(NICKNAME));
    assertTrue(passwordEncoder.matches(PASSWORD, userArgument.getValue().getPassword()));
  }
  
  @Test
  public void deleteUser() {
    // given
    UserDetails principal = new org.springframework.security.core.userdetails.User(
        EMAIL, PASSWORD, new ArrayList<GrantedAuthority>());
    given(securityContextUtilMock.getPrincipal()).willReturn(principal);
    
    User user = new User(EMAIL);
    given(userRepositoryMock.findByEmail(EMAIL)).willReturn(user);

    // when
    userService.deleteUser();

    // then
    verify(userRepositoryMock, times(1)).findByEmail(EMAIL);
    verify(userRepositoryMock, times(1)).delete((User)any());
    verifyNoMoreInteractions(userRepositoryMock);
  }
}