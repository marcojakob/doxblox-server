package ch.documakery.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.documakery.UserTestUtils;
import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.repository.UserRepository;
import ch.documakery.security.util.SecurityContextUtil;

/**
 * Test for {@link UserServiceImpl}.
 * 
 * @author Marco Jakob
 */
public class UserServiceImplTest {

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
  public void getUser_UserIsLoggedIn_ReturnsUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);

    // when
    User loggedInUser = userService.getUser();

    // then
    verify(securityContextUtilMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    assertThat(loggedInUser.getEmail(), is(UserTestUtils.EMAIL));
  }

  @Test
  public void getUser_UserIsNotLoggedIn_ReturnsNull() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(null);

    // when
    User loggedInUser = userService.getUser();

    // then
    verify(securityContextUtilMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    verifyZeroInteractions(userRepositoryMock);

    assertThat(loggedInUser, is(nullValue()));
  }

  @Test
  public void registerUser() {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(UserTestUtils.EMAIL);
    userRegister.setNickname(UserTestUtils.NICKNAME);
    userRegister.setPassword(UserTestUtils.PASSWORD);

    // when
    userService.registerUser(userRegister);

    // then
    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    verify(userRepositoryMock, times(1)).save(userArgument.capture());
    verifyNoMoreInteractions(userRepositoryMock);
    
    assertThat(userArgument.getValue().isEmailConfirmed(), is(false));
    assertThat(userArgument.getValue().getEmail(), is(UserTestUtils.EMAIL));
    assertThat(userArgument.getValue().getNickname(), is(UserTestUtils.NICKNAME));
    assertTrue(passwordEncoder.matches(UserTestUtils.PASSWORD, userArgument.getValue().getPassword()));
  }
  
  @Test
  public void deleteUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    userService.deleteUser();

    // then
    verify(securityContextUtilMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    verify(userRepositoryMock, times(1)).delete((User)any());
    verifyNoMoreInteractions(userRepositoryMock);
  }
}