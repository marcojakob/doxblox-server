package ch.doxblox.domain.document;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.doxblox.domain.document.question.QuestionBlock;

import com.google.common.base.Strings;

/**
 * Validation test for {@link QuestionBlock} domain class.
 * 
 * @author Marco Jakob
 */
public class QuestionBlockTest {
  
  static LocalValidatorFactoryBean validator;

  @BeforeClass
  public static void setUp() {
    validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
  }
  
  @Test
  public void validate_NoErrors() {
    // given
    QuestionBlock block = new QuestionBlock();
    block.setTitle("a Title");
    Errors errors = new BeanPropertyBindingResult(block, "");

    // when
    validator.validate(block, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_TitleNull_Error() {
    // given
    QuestionBlock block = new QuestionBlock();
    Errors errors = new BeanPropertyBindingResult(block, "");

    // when
    validator.validate(block, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("NotBlank"));
    assertThat(errors.getFieldError().getField(), is("title"));
  }
  
  @Test
  public void validate_TitleTooLong_Error() {
    // given
    QuestionBlock block = new QuestionBlock();
    block.setTitle(Strings.repeat("Z", 121));
    Errors errors = new BeanPropertyBindingResult(block, "");
    
    // when
    validator.validate(block, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Size"));
    assertThat(errors.getFieldError().getField(), is("title"));
  }
}
