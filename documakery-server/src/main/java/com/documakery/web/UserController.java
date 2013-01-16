package com.documakery.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.documakery.dto.User;
import com.documakery.service.UserService;

/**
 * Controller for user actions.
 */
@Controller
public class UserController {
    @Inject
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }
}
