package ch.documakery.domain.user.dto;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.documakery.domain.user.User;
import ch.documakery.repository.UserRepository;

/**
 * Test for {@link UserRegisterDto}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
  "classpath:/META-INF/spring/testContext-mockRepositories.xml",
  "classpath:/META-INF/spring/testContext-validator.xml"})
public class UserRegisterDtoTest {
  
  @Inject
  LocalValidatorFactoryBean validator;

  @Inject
  UserRepository userRepositoryMock;
  
  @Before
  public void setUp() {
    // Injected mock is a singleton and must therefore be reset before each test
    reset(userRepositoryMock);
  }
  
  @Test
  public void validate_NoErrors() {
    // given
    UserRegisterDto dto = new UserRegisterDto("email@email.com", "nickname", "password");
    userRepositoryMock.findByEmail("email@email.com");
    Errors errors = new BeanPropertyBindingResult(dto, "dto");

    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_EmailNotUnique_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto("email@email.com", "nickname", "password");
    given(userRepositoryMock.findByEmail(anyString())).willReturn(new User("email@email.com"));
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("UniqueEmail"));
  }
  
  @Test
  public void validate_EmailNull_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto(null, "nickname", "password");
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("NotBlank"));
    assertThat(errors.getFieldError().getField(), is("email"));
  }
  
  @Test
  public void validate_EmailInvalid_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto("inv@lid@mail.com", "nickname", "password");
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Email"));
    assertThat(errors.getFieldError().getField(), is("email"));
  }
  
  @Test
  public void validate_NicknameNotUnique_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto("email@email.com", "nickname", "password");
    given(userRepositoryMock.findByNickname("nickname")).willReturn(new User("email@email.com"));
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("UniqueNickname"));
    assertThat(errors.getFieldError().getField(), is("nickname"));
  }
  
  @Test
  public void validate_NicknameTooLarge_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto("email@email.com", "26letters_is_too_much!!!!!", "pasword");
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Size"));
    assertThat(errors.getFieldError().getField(), is("nickname"));
  }
  
  @Test
  public void validate_PasswordTooSmall_Error() {
    // given
    UserRegisterDto dto = new UserRegisterDto("email@email.com", "nickname", "pas");
    Errors errors = new BeanPropertyBindingResult(dto, "dto");
    
    // when
    validator.validate(dto, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Size"));
    assertThat(errors.getFieldError().getField(), is("password"));
  }
}
