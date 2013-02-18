package com.documakery.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

import com.documakery.domain.user.User;
import com.documakery.domain.user.dto.UserRegisterDto;

/**
 * Service to handle Users.
 */
public interface UserService {

  /**
   * Returns the currently authenticated user. If {@link UserDetails} are sufficient you should call
   * {@link #getPrincipal()} instead since it will not require a db call.
   * 
   * @return The current user.
   */
  @PreAuthorize("isAuthenticated()")
  User getUser();
  
  /**
   * Returns the current authenticated principal. 
   * 
   * @return The current user.
   */
  @PreAuthorize("isAuthenticated()")
  UserDetails getPrincipal();

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
  User register(UserRegisterDto userRegister);
  
  /**
   * Deletes the current user.
   */
  @PreAuthorize("isAuthenticated()")
  void deleteUser();
}
