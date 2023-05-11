package com.example.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"user", "createDate", "updateDate"})
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    private String title;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public List(String title) {
        this.title = title;
        this.createDate = LocalDateTime.now();
    }
}
