package ch.documakery.validation.constraints.impl;

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
import ch.documakery.validation.constraints.UniqueEmail;

/**
 * Test for {@link UniqueEmailValidator}.
 * 
 * @author Marco Jakob
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:/META-INF/spring/testContext-mockRepositories.xml",
    "classpath:/META-INF/spring/testContext-validator.xml"})
public class UniqueEmailValidatorTest {
  
  private class TestBean {
    @UniqueEmail
    String email;
    
    TestBean(String email) {
      this.email = email;
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
  public void validate_EmailUnique_NoErrors() {
    // given
    String email = "unique@email.com";
    TestBean bean = new TestBean(email);
    given(userRepositoryMock.findByEmail(email)).willReturn(null);
    
    Errors errors = new BeanPropertyBindingResult(bean, "bean");

    // when
    validator.validate(bean, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_EmailNotUnique_ReportError() {
    // given
    String email = "not_unique@email.com";
    TestBean bean = new TestBean(email);
    given(userRepositoryMock.findByEmail(email)).willReturn(new User(email, "anypassword"));
    
    Errors errors = new BeanPropertyBindingResult(bean, "bean");
    
    // when
    validator.validate(bean, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("UniqueEmail"));
  }
}
