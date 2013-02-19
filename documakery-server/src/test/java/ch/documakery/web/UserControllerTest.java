package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserDto;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.service.UserService;
import ch.documakery.web.UserController;

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
  public void getUser() {
    // given
    UserDetails user = new org.springframework.security.core.userdetails.User(
        EMAIL, PASSWORD, new ArrayList<GrantedAuthority>());

    given(userServiceMock.getPrincipal()).willReturn(user);

    // when
    UserDto loggedInUser = userController.getUser();

    // then
    verify(userServiceMock, times(1)).getPrincipal();
    verifyNoMoreInteractions(userServiceMock);

    assertThat(loggedInUser.getEmail(), is(EMAIL));
  }

  @Test
  public void getUser_UserIsNotLoggedIn() {
    // given
    given(userServiceMock.getUser()).willReturn(null);

    // when
    UserDto loggedInUser = userController.getUser();

    // then
    verify(userServiceMock, times(1)).getPrincipal();
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
    given(userServiceMock.register(userRegister)).willReturn(new User(EMAIL));

    // when
    UserDto response = userController.registerUser(userRegister);

    // then
    verify(userServiceMock, times(1)).register(userRegister);
    verifyNoMoreInteractions(userServiceMock);

    assertThat(response.getEmail(), is(EMAIL));
  }
}