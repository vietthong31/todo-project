package com.example.todo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String content;
    private LocalDate dueDate;
    private LocalDate createDate;
    private boolean isCompleted;

    public TaskDTO(String content, LocalDate dueDate) {
        this.content = content;
        this.dueDate = dueDate;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean completed) {
        isCompleted = completed;
    }
}
