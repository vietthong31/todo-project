package com.example.todo.service;

import com.example.todo.entity.User;
import com.example.todo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public boolean isUserLoggedIn(Authentication auth) {
        return auth != null && auth.isAuthenticated();
    }

    public void save(User user) {
        repository.save(user);
    }

    public List<String> validateUser(User user) {
        List<String> messages = new ArrayList<>();
        if (repository.existsByEmail(user.getEmail())) {
            messages.add("Email already exists.");
        }
        if (repository.existsByUsername(user.getUsername())) {
            messages.add("Username already exists.");
        }
        return messages;
    }


}
