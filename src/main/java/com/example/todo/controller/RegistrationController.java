package com.example.todo.controller;

import com.example.todo.repository.UserRepository;
import com.example.todo.security.RegistrationForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute(name = "form")
    public RegistrationForm form() {
        return new RegistrationForm();
    }

    @GetMapping
    public String registerForm(Authentication auth) {
        if (auth!= null && auth.isAuthenticated()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping
    public String processRegistration(@Valid @ModelAttribute(name = "form") RegistrationForm form, Errors errors) {
        if (errors.hasErrors()) {
            return "register";
        }
        userRepository.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }

}
