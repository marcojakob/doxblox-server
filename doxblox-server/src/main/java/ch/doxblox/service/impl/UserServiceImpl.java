package ch.doxblox.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.doxblox.domain.user.User;
import ch.doxblox.domain.user.dto.UserRegisterDto;
import ch.doxblox.repository.UserRepository;
import ch.doxblox.security.util.SecurityContextUtil;
import ch.doxblox.service.UserService;

/**
 * Service to access {@link User}s in MongoDB repository.
 * 
 * @author Marco Jakob
 */
@Service
public class UserServiceImpl implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;
  private final SecurityContextUtil securityContextUtil;
  private final PasswordEncoder passwordEncoder;

  @Inject
  public UserServiceImpl(UserRepository userRepository, SecurityContextUtil securityContextUtil,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.securityContextUtil = securityContextUtil;
    this.passwordEncoder = passwordEncoder;
  }
  
  @Override
  public User getUser() {
    User user = securityContextUtil.getCurrentUser();
    LOG.debug("Getting current user from security context: {}", user);
    return user;
  }

  @Override
  public User registerUser(UserRegisterDto userRegister) {
    // Encode the password
    String encodedPassword = passwordEncoder.encode(userRegister.getPassword());
    
    User user = new User(userRegister.getEmail(), encodedPassword);
    user.setNickname(userRegister.getNickname());
    
    userRepository.save(user);

    // TODO: Send registration mail
    // registrationMailService.sendConfirmationMail(user);
    
    return user;
  }

  @Override
  @PreAuthorize("isAuthenticated()")
  public void deleteUser() {
    User user = getUser();
    if (user != null) {
      LOG.info("Deleting user from db: {}", user.getEmail());
      userRepository.delete(user);
    } else {
      LOG.warn("Current user was null.");
    }
  }
}
