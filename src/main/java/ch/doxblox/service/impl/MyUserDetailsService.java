package ch.doxblox.service.impl;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.doxblox.domain.user.User;
import ch.doxblox.repository.UserRepository;
import ch.doxblox.security.authentication.EmailNotConfirmedException;

import com.google.common.base.Preconditions;

/**
 * Loads the user from the MongoDB repository (by email) and provides it to Spring. This is
 * primarily used by {@link SecurityContextHolder} to get access to the {@link UserDetails}.
 * 
 * @author Marco Jakob
 */
@Service
public final class MyUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  @Inject
  public MyUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public final UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Preconditions.checkNotNull(username);

    User user = userRepository.findByEmail(username);

    if (user == null) {
      throw new UsernameNotFoundException("Email was not found: " + username);
    }
    
    if (!user.isEmailConfirmed()) {
      throw new EmailNotConfirmedException("Email not confirmed");
    }
    
    return user;
  }
}