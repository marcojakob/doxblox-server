package ch.doxblox.validation.constraints.impl;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ch.doxblox.repository.UserRepository;
import ch.doxblox.validation.constraints.UniqueNickname;

/**
 * Check that the nickname is unique in the db.
 * 
 * @author Marco Jakob
 */
public class UniqueNicknameValidator implements ConstraintValidator<UniqueNickname, String> {

  private UserRepository userRepository;

  @Inject
  public UniqueNicknameValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  @Override
  public void initialize(UniqueNickname annotation) {
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Must not exist in the db
    return userRepository.findByNickname(value) == null;
  }
}
