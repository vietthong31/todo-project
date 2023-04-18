package com.example.todo.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public boolean isUserLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }
}
