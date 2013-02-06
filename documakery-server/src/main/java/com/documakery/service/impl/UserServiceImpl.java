package com.documakery.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.documakery.domain.user.User;
import com.documakery.domain.user.User.AccountStatus;
import com.documakery.domain.user.dto.UserDto;
import com.documakery.domain.user.dto.UserRegisterDto;
import com.documakery.repository.UserRepository;
import com.documakery.security.util.SecurityContextUtil;
import com.documakery.service.UserService;

/**
 * 
 * UserService that accesses the spring credentials.
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
  public UserDto getCurrentUser() {
    LOG.debug("Getting logged in user.");
    UserDetails principal = securityContextUtil.getPrincipal();

    return createDTO(principal);
  }

  private UserDto createDTO(UserDetails principal) {
    UserDto dto = null;
    if (principal != null) {
      String email = principal.getUsername();

      dto = new UserDto(email, "no nickname");
    }

    LOG.debug("Created user dto: {}", dto);

    return dto;
  }

  @Override
  public void register(UserRegisterDto userRegister) {
    User user = new User(userRegister.getEmail());
    user.setAccountStatus(AccountStatus.EMAIL_UNCONFIRMED);
    user.setNickname(userRegister.getNickname());
    
    // Encode the password
    user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
    
    userRepository.save(user);

    // registrationMailService.sendConfirmationMail(user);
  }
}
