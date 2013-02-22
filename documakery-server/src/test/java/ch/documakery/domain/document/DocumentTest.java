package ch.documakery.domain.document;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Validation test for {@link Document}.
 * 
 * @author Marco Jakob
 */
public class DocumentTest {
  
  static LocalValidatorFactoryBean validator;

  @BeforeClass
  public static void setUp() {
    validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
  }
  
  @Test
  public void validate_NoErrors() {
    // given
    Document doc = new Document();
    doc.setName("My supi-dupi Doc");
    Errors errors = new BeanPropertyBindingResult(doc, "doc");

    // when
    validator.validate(doc, errors);
    
    // then
    assertThat(errors.hasErrors(), is(false));
  }
  
  @Test
  public void validate_NameNull_Error() {
    // given
    Document doc = new Document();
    Errors errors = new BeanPropertyBindingResult(doc, "doc");

    // when
    validator.validate(doc, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("NotBlank"));
    assertThat(errors.getFieldError().getField(), is("name"));
  }
  
  @Test
  public void validate_NameTooLong_Error() {
    // given
    Document doc = new Document();
    doc.setName("12345678901234567890123456789012345678901234567890123456789012345678901234567890A");
    Errors errors = new BeanPropertyBindingResult(doc, "doc");
    
    // when
    validator.validate(doc, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("Size"));
    assertThat(errors.getFieldError().getField(), is("name"));
  }
  
  @Test
  public void validate_DocumentBlockIdsIsNull_Error() {
    // given
    Document doc = new Document();
    doc.setDocumentBlockIds(null);
    doc.setName("name");
    Errors errors = new BeanPropertyBindingResult(doc, "doc");
    
    // when
    validator.validate(doc, errors);
    
    // then
    assertThat(errors.getErrorCount(), is(1));
    assertThat(errors.getFieldError().getCode(), is("NotNull"));
    assertThat(errors.getFieldError().getField(), is("documentBlockIds"));
  }
}
