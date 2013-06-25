package ch.doxblox.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.doxblox.UserTestUtils;
import ch.doxblox.domain.user.User;
import ch.doxblox.domain.user.dto.UserRegisterDto;
import ch.doxblox.repository.UserRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.impl.UserServiceImpl;

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
  public void getUser_AsUser_ReturnsUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);

    // when
    User loggedInUser = userService.getUser();

    // then
    verify(securityContextUtilMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    assertThat(loggedInUser.getEmail(), is(UserTestUtils.TEST_USER_EMAIL));
  }

  @Test
  public void getUser_AsAnonymous_ReturnsNull() {
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
  public void registerUser_AsAnonymous_SaveCalledWithHashedPassword() {
    // given
    UserRegisterDto userRegister = new UserRegisterDto();
    userRegister.setEmail(UserTestUtils.TEST_USER_EMAIL);
    userRegister.setNickname(UserTestUtils.TEST_USER_NICKNAME);
    userRegister.setPassword(UserTestUtils.TEST_USER_PASSWORD);

    // when
    userService.registerUser(userRegister);

    // then
    ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
    verify(userRepositoryMock, times(1)).save(userArgument.capture());
    verifyNoMoreInteractions(userRepositoryMock);
    
    assertThat(userArgument.getValue().isEmailConfirmed(), is(false));
    assertThat(userArgument.getValue().getEmail(), is(UserTestUtils.TEST_USER_EMAIL));
    assertThat(userArgument.getValue().getNickname(), is(UserTestUtils.TEST_USER_NICKNAME));
    assertTrue(passwordEncoder.matches(UserTestUtils.TEST_USER_PASSWORD, userArgument.getValue().getPassword()));
  }
  
  @Test
  public void deleteUser_AsUser_DeleteUserCalledWithCurrentUser() {
    // given
    given(securityContextUtilMock.getCurrentUser()).willReturn(UserTestUtils.TEST_USER);
    
    // when
    userService.deleteUser();

    // then
    verify(securityContextUtilMock, times(1)).getCurrentUser();
    verifyNoMoreInteractions(securityContextUtilMock);
    
    verify(userRepositoryMock, times(1)).delete(UserTestUtils.TEST_USER);
    verifyNoMoreInteractions(userRepositoryMock);
  }
}