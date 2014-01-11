package ch.doxblox.domain.document;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import ch.doxblox.domain.document.Document;
import ch.doxblox.domain.document.DocumentFolder;

import com.google.common.base.Strings;

/**
 * Validation test for {@link Document} domain class.
 * 
 * @author Marco Jakob
 */
public class DocumentFolderTest {
  
  static LocalValidatorFactoryBean validator;

  @BeforeClass
  public static void setUp() {
    validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
  }
  
  @Test
  public void validate_NoErrors() {
    // given
    DocumentFolder folder = new DocumentFolder("Schoolyear 2012/2013");
    Errors errors = new BeanPropertyBindingResult(folder, "");

    // when
    validator.validate(folder, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_NameNull_Error() {
    // given
    DocumentFolder folder = new DocumentFolder(null);
    Errors errors = new BeanPropertyBindingResult(folder, "");

    // when
    validator.validate(folder, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("NotBlank"));
    assertThat(errors.getFieldError().getField(), is("name"));
  }
  
  @Test
  public void validate_NameTooLong_Error() {
    // given
    DocumentFolder folder = new DocumentFolder(Strings.repeat("A", 51));
    Errors errors = new BeanPropertyBindingResult(folder, "doc");
    
    // when
    validator.validate(folder, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Size"));
    assertThat(errors.getFieldError().getField(), is("name"));
  }
}
