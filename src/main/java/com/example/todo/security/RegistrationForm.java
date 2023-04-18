package com.example.todo.security;

import com.example.todo.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegistrationForm {
    @Email
    private String email;

    @NotBlank(message = "Username must not be blank.")
    @Size(max = 50, message = "Username length mustn't be greater than 50 characters.")
    private String username;

    @Size(min = 8, message = "Password length must be at least 8 characters.")
    @Size(max = 255, message = "Password length must be between 8 and 255 characters.")
    private String password;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(email, username, passwordEncoder.encode(password));
    }
}
