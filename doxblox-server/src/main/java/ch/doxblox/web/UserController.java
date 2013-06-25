package ch.doxblox.web;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.doxblox.domain.user.User;
import ch.doxblox.domain.user.dto.UserDto;
import ch.doxblox.domain.user.dto.UserRegisterDto;
import ch.doxblox.service.UserService;

/**
 * Rest access to {@link User}s.
 */
@Controller
public class UserController {
  
  private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
  
  private final UserService userService;
  
  @Inject
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  @ResponseBody
  public UserDto getUser() {
    User user = userService.getUser();
    if (user == null) {
      return null;
    }
    LOG.debug("Returning current user: {}", user.getUsername());
    return new UserDto(user);
  }

  @RequestMapping(value = "/users", method = RequestMethod.POST)
  @ResponseBody
  public UserDto registerUser(@RequestBody @Valid UserRegisterDto userRegister) {
    LOG.debug("Registering user: {}", userRegister.getEmail());
    User user = userService.registerUser(userRegister);
    return new UserDto(user);
  }
  
  @RequestMapping(value = "/users", method = RequestMethod.DELETE)
  @ResponseBody
  public void deleteUser() {
    LOG.info("Deleting user.");
    userService.deleteUser();
  }
  
}
