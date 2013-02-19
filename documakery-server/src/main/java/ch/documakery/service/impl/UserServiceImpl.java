package ch.documakery.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.repository.UserRepository;
import ch.documakery.security.util.SecurityContextUtil;
import ch.documakery.service.UserService;

/**
 * UserService that accesses the spring credentials.
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
    UserDetails principal = getPrincipal();
    if (principal != null) {
      LOG.debug("Getting user from db: {}", principal.getUsername());
      return userRepository.findByEmail(principal.getUsername());
    } else {
      LOG.warn("No principal.");
      return null;
    }
  }

  @Override
  public UserDetails getPrincipal() {
    LOG.debug("Getting logged in user from security context.");
    UserDetails principal = securityContextUtil.getPrincipal();

    return principal;
  }

  @Override
  public User register(UserRegisterDto userRegister) {
    User user = new User(userRegister.getEmail());
    user.setNickname(userRegister.getNickname());
    
    // Encode the password
    user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
    
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
      LOG.debug("Deleting user from db: {}", user.getEmail());
      userRepository.delete(user);
    } else {
      LOG.warn("Current user was null.");
    }
  }
}
