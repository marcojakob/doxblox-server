package ch.doxblox.service;

import org.springframework.security.access.prepost.PreAuthorize;

import ch.doxblox.domain.user.User;
import ch.doxblox.domain.user.dto.UserRegisterDto;

/**
 * Service for managing {@link User}s.   
 * 
 * @author Marco Jakob
 */
public interface UserService {

  /**
   * Returns the currently authenticated user. 
   * 
   * @return The current user.
   */
  @PreAuthorize("isAuthenticated()")
  public User getUser();
  
  /**
   * Registers the user.
   * <p/>
   * At this point, the provided {@link UserRegisterDto} must have been validated, so that the user
   * would have been informed about violations already. The validation should include a uniqueness
   * check of nickname and email. Though the uniqueness of email and nickname is guaranteed by the 
   * database, no error will be thrown by this method.
   * 
   * @param userRegister User registration information.
   */
  public User registerUser(UserRegisterDto userRegister);
  
  /**
   * Deletes the current user.
   */
  @PreAuthorize("isAuthenticated()")
  public void deleteUser();
}
