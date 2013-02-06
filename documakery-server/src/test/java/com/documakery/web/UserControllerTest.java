package com.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;

import com.documakery.domain.user.dto.UserDto;
import com.documakery.domain.user.dto.UserRegisterDto;
import com.documakery.dto.ResponseMessage;
import com.documakery.service.UserService;

/**
 * Test for {@link UserController}.
 * 
 * @author Marco Jakob
 */
public class UserControllerTest {

  private static final String EMAIL = "user@user.com";
  private static final String PASSWORD = "password";
  private static final String NICKNAME = "nickname";

  private UserController userController; // SUT

  private UserService userServiceMock;

  @Before
  public void setUp() {
    userServiceMock = mock(UserService.class);
    userController = new UserController(userServiceMock);
  }

  @Test
  public void getCurrentUser() {
    // given
    UserDto userDto = new UserDto(EMAIL, NICKNAME);

    given(userServiceMock.getCurrentUser()).willReturn(userDto);

    // when
    UserDto loggedInUser = userController.getCurrentUser();

    // then
    verify(userServiceMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(userServiceMock);

    assertThat(EMAIL, is(loggedInUser.getEmail()));
    assertThat(NICKNAME, is(loggedInUser.getNickname()));
  }

  @Test
  public void getCurrentUser_UserIsNotLoggedIn() {
    // given
    given(userServiceMock.getCurrentUser()).willReturn(null);

    // when
    UserDto loggedInUser = userController.getCurrentUser();

    // then
    verify(userServiceMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(userServiceMock);

    assertThat(loggedInUser, is(nullValue()));
  }

  @Test
  public void registerUser() {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(EMAIL);
    userRegister.setNickname(NICKNAME);
    userRegister.setPassword(PASSWORD);
    userRegister.setPasswordConfirm(PASSWORD);

    // when
    ResponseMessage response = userController.registerUser(userRegister);

    // then
    verify(userServiceMock, times(1)).register(userRegister);
    verifyNoMoreInteractions(userServiceMock);

    assertThat(response.getType(), is(ResponseMessage.Type.success));
  }
}