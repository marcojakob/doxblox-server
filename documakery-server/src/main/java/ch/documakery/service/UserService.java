package ch.documakery.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;

import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserRegisterDto;

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
   * check of nickname and email. Though the uniqueness of email and nickname is guaranteed by the 
   * db, no error will be thrown by this method.
   * 
   * @param userRegister User registration information.
   */
  User register(UserRegisterDto userRegister);
  
  /**
   * Deletes the current user.
   */
  @PreAuthorize("isAuthenticated()")
  void deleteUser();
}
