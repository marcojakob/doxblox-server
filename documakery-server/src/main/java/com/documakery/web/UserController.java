package com.documakery.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.documakery.domain.user.dto.UserDto;
import com.documakery.domain.user.dto.UserRegisterDto;
import com.documakery.dto.ResponseMessage;
import com.documakery.service.UserService;

/**
 * Controller for user actions.
 */
@Controller
public class UserController {
  
  private final UserService userService;
  
  @Inject
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  @ResponseBody
  public UserDto getCurrentUser() {
    return userService.getCurrentUser();
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
  @ResponseBody
  public ResponseMessage registerUser(@RequestBody UserRegisterDto userRegister) {
    userService.register(userRegister);
    return new ResponseMessage(ResponseMessage.Type.success, "userRegistered");
  }
}
