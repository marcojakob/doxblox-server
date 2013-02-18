package com.documakery.validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.documakery.validation.constraints.impl.UniqueEmailValidator;

/**
 * The annotated e-mail address must be unique inside the database.
 * <p/>
 * <b>Note</b>: Constraint is designed to only run on the server!
 * 
 * @author Marco Jakob
 */
@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface UniqueEmail {
  String message() default "Email not unique";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

  /**
   * Defines several {@link UniqueEmail} annotations on the same element.
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  public @interface List {
    UniqueEmail[] value();
  }
}
