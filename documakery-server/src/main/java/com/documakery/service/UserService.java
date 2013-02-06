package com.documakery.service;

import org.springframework.security.access.prepost.PreAuthorize;

import com.documakery.domain.user.dto.UserDto;
import com.documakery.domain.user.dto.UserRegisterDto;

/**
 * Service to handle Users.
 */
public interface UserService {

  /**
   * Returns the current user that is either authenticated or remembered.
   * 
   * @return The current user.
   */
  @PreAuthorize("isAuthenticated()")
  UserDto getCurrentUser();

  /**
   * Registers the user.
   * <p/>
   * At this point, the provided {@link UserRegisterDto} must have been validated, so that the user
   * would have been informed about violations already. The validation should include a uniqueness
   * check of nickname since this will not be checked by this method. This leaves a very small
   * chance that the same nickname would be registered twice. However, the uniqueness of the email
   * is guaranteed and will throw an error if it is not unique.
   * 
   * @param userRegister User registration information.
   * @throws EmailAlreadyExistsException if the user could not be created because the email already
   *           exists.
   */
  void register(UserRegisterDto userRegister);
}
