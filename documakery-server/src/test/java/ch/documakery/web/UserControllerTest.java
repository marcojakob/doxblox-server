package ch.documakery.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;

import ch.documakery.UserTestUtils;
import ch.documakery.domain.user.dto.UserDto;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.service.UserService;

/**
 * Test for {@link UserController}.
 * 
 * @author Marco Jakob
 */
public class UserControllerTest {

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
    given(userServiceMock.getUser()).willReturn(UserTestUtils.TEST_USER);

    // when
    UserDto loggedInUser = userController.getUser();

    // then
    verify(userServiceMock, times(1)).getUser();
    verifyNoMoreInteractions(userServiceMock);

    assertThat(loggedInUser.getEmail(), is(UserTestUtils.EMAIL));
  }

  @Test
  public void getUser_UserIsNotLoggedIn() {
    // given
    given(userServiceMock.getUser()).willReturn(null);

    // when
    UserDto loggedInUser = userController.getUser();

    // then
    verify(userServiceMock, times(1)).getUser();
    verifyNoMoreInteractions(userServiceMock);

    assertThat(loggedInUser, is(nullValue()));
  }

  @Test
  public void registerUser() {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(UserTestUtils.EMAIL);
    userRegister.setNickname(UserTestUtils.NICKNAME);
    userRegister.setPassword(UserTestUtils.PASSWORD);
    given(userServiceMock.register(userRegister)).willReturn(UserTestUtils.TEST_USER);

    // when
    UserDto response = userController.registerUser(userRegister);

    // then
    verify(userServiceMock, times(1)).register(userRegister);
    verifyNoMoreInteractions(userServiceMock);

    assertThat(response.getEmail(), is(UserTestUtils.EMAIL));
  }
}