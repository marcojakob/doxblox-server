package com.documakery.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.documakery.domain.user.User;
import com.documakery.repository.UserRepository;
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

    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), authorities);
  }
}