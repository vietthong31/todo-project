package com.example.todo.controller;

import com.example.todo.entity.User;
import com.example.todo.security.RegistrationForm;
import com.example.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute(name = "form")
    public RegistrationForm form() {
        return new RegistrationForm();
    }

    @GetMapping
    public String registerForm(Authentication auth) {
        return (userService.isUserLoggedIn(auth)) ? "redirect:/task" : "register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute(name = "form") RegistrationForm form, BindingResult result) {
        User user = form.toUser(passwordEncoder);
        List<String> errors = userService.validateUser(user);
        if (!errors.isEmpty()) {
            errors.forEach(error -> result.addError(new ObjectError("globalError", error)));
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerNewUserAccount(user);
        return "redirect:/login";
    }

}
