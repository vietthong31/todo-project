package com.example.todo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tasklist")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    private List list;

    @OneToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
