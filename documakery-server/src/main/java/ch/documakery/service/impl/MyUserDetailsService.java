package ch.documakery.service.impl;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.documakery.domain.user.User;
import ch.documakery.repository.UserRepository;
import ch.documakery.security.authentication.EmailNotConfirmedException;

import com.google.common.base.Preconditions;

/**
 * Database user authentication service.
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