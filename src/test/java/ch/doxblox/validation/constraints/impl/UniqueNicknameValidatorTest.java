package ch.doxblox.validation.constraints.impl;

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

import ch.doxblox.domain.user.User;
import ch.doxblox.repository.UserRepository;
import ch.doxblox.validation.constraints.UniqueNickname;
import ch.doxblox.validation.constraints.impl.UniqueNicknameValidator;

/**
 * Test for {@link UniqueNicknameValidator}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
  "classpath:/META-INF/spring/testContext-mockRepositories.xml",
  "classpath:/META-INF/spring/testContext-validator.xml"})
public class UniqueNicknameValidatorTest {
  
  private class TestBean {
    @UniqueNickname
    String nickname;
    
    TestBean(String nickname) {
      this.nickname = nickname;
    }
  }
  
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
  public void validate_NicknameUnique_NoErrors() {
    // given
    String nickname = "uniqueNickname";
    TestBean bean = new TestBean(nickname);
    given(userRepositoryMock.findByNickname(nickname)).willReturn(null);
    
    Errors errors = new BeanPropertyBindingResult(bean, "bean");

    // when
    validator.validate(bean, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_NicknameNotUnique_ReportError() {
    // given
    String nickname = "notUniqueNickname";
    TestBean bean = new TestBean(nickname);
    given(userRepositoryMock.findByNickname(nickname)).willReturn(new User("user", "anypassword"));
    
    Errors errors = new BeanPropertyBindingResult(bean, "bean");
    
    // when
    validator.validate(bean, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("UniqueNickname"));
  }
}
