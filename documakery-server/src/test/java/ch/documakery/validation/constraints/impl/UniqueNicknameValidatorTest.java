package ch.documakery.validation.constraints.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.documakery.domain.user.User;
import ch.documakery.repository.UserRepository;
import ch.documakery.validation.constraints.UniqueNickname;

/**
 * Test for {@link UniqueNicknameValidator}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("UniqueValidatorTest-context.xml")
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
  public void validate_EmailNotUnique_ReportError() {
    // given
    String nickname = "notUniqueNickname";
    TestBean bean = new TestBean(nickname);
    given(userRepositoryMock.findByNickname(nickname)).willReturn(new User(""));
    
    Errors errors = new BeanPropertyBindingResult(bean, "bean");
    
    // when
    validator.validate(bean, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("UniqueNickname"));
  }
}
