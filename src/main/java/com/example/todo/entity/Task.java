package com.example.todo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"list", "createDate", "updateDate"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "list_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List list;

    private String content;
    private LocalDateTime dueDate;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private boolean isCompleted;

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Task(List list, String content, LocalDateTime dueDate) {
        this.list = list;
        this.content = content;
        this.dueDate = dueDate;
    }

    public Task(List list, String content, LocalDateTime dueDate, boolean isCompleted) {
        this(list, content, dueDate);
        this.isCompleted = isCompleted;
    }
}
