package ch.documakery.web;

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

import ch.documakery.domain.user.User;
import ch.documakery.domain.user.dto.UserDto;
import ch.documakery.domain.user.dto.UserRegisterDto;
import ch.documakery.service.UserService;

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
    if (principal == null) {
      return null;
    }
    LOG.debug("Getting user for principal: {}", principal.getUsername());
    return new UserDto(principal);
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseBody
  public UserDto registerUser(@RequestBody @Valid UserRegisterDto userRegister) {
    LOG.debug("Registering user: {}", userRegister.getEmail());
    User user = userService.register(userRegister);
    return new UserDto(user);
  }
  
  @RequestMapping(value = "/user", method = RequestMethod.DELETE)
  @ResponseBody
  public void deleteUser() {
    LOG.info("Deleging user.");
    userService.deleteUser();
  }
  
}
