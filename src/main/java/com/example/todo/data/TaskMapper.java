package com.example.todo.data;

import com.example.todo.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setContent(taskDTO.getContent());
        task.setDueDate(taskDTO.getDueDate());
        return task;
    }

    public TaskDTO toTaskDTO(Task task) {
        return new TaskDTO(task.getId(), task.getContent(), task.getDueDate(), task.getDueDate(),task.getIsCompleted());
    }
}
