package ch.documakery.validation.constraints.impl;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ch.documakery.repository.UserRepository;
import ch.documakery.validation.constraints.UniqueEmail;

/**
 * Check that the email is unique in the db.
 * 
 * @author Marco Jakob
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  private UserRepository userRepository;

  @Inject
  public UniqueEmailValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  @Override
  public void initialize(UniqueEmail annotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Must not exist in the db
    return userRepository.findByEmail(value) == null;
  }
}
