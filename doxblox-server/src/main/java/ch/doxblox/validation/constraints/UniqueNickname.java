package ch.doxblox.validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ch.doxblox.validation.constraints.impl.UniqueNicknameValidator;

/**
 * The annotated nickname must be unique inside the database.
 * <p/>
 * <b>Note</b>: Constraint is designed to only run on the server!
 * 
 * @author Marco Jakob
 */
@Documented
@Constraint(validatedBy = UniqueNicknameValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface UniqueNickname {
  String message() default "Nickname not unique";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * Defines several {@link UniqueNickname} annotations on the same element.
   */
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  public @interface List {
    UniqueNickname[] value();
  }
}
