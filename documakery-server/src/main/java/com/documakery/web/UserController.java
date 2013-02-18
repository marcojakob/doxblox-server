package com.documakery.web;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.documakery.domain.user.User;
import com.documakery.domain.user.dto.UserDto;
import com.documakery.domain.user.dto.UserRegisterDto;
import com.documakery.service.UserService;

/**
 * Controller for user actions.
 */
@Controller
public class UserController {
  
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  
  private final UserService userService;
  
  @Inject
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public UserDto getUser() {
    UserDetails principal = userService.getPrincipal();
    return createDto(principal);
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseBody
  public UserDto registerUser(@RequestBody @Valid UserRegisterDto userRegister) {
    User user = userService.register(userRegister);
    return createDto(user);
  }
  
  @RequestMapping(value = "/user", method = RequestMethod.DELETE)
  @ResponseBody
  public void deleteUser() {
    LOG.info("Deleging user.");
    userService.deleteUser();
  }
  
  private UserDto createDto(UserDetails principal) {
    UserDto dto = null;
    if (principal != null) {
      dto = new UserDto(principal.getUsername(), "no nickname");
    }
    LOG.debug("Created UserDto: {}", dto);
    return dto;
  }
  
  private UserDto createDto(User user) {
    UserDto dto = null; {
      dto = new UserDto(user.getEmail(), user.getNickname());
    }
    LOG.debug("Created UserDto: {}", dto);
    return dto;
  }
}
