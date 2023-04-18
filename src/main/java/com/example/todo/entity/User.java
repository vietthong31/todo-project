package com.example.todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    private LocalDate createDate = LocalDate.now();
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String username;
    private String encryptedPassword;
    private LocalDate lastUpdate;

    public User(String email, String username, String encryptedPassword) {
        this.email = email;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }
}
