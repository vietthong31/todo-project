package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    @GetMapping("/account")
    public String account() {
        return "account";
    }

    @GetMapping("/password")
    public String password() {
        return "change_password";
    }
}
