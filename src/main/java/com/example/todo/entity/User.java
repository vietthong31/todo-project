package com.example.todo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class User {
    private LocalDate createDate = LocalDate.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String encryptedPassword;
    private LocalDate lastUpdate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Task> tasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<List> lists;

    public User(String email, String username, String encryptedPassword) {
        this.email = email;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }
}
