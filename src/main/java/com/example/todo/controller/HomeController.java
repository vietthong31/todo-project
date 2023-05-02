package com.example.todo.controller;

import com.example.todo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Authentication auth) {
        return (userService.isUserLoggedIn(auth)) ? "redirect:/task" : "index";
    }

    @GetMapping("/login")
    public String login(Authentication auth) {
        return userService.isUserLoggedIn(auth) ? "redirect:/" : "login";
    }

}
