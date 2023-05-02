package com.example.todo.service;

import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final Validator validator;

    @Autowired
    public TaskService(TaskRepository taskRepository, Validator validator) {
        this.taskRepository = taskRepository;
        this.validator = validator;
    }

    public Iterable<Task> completedTasks() {
        return taskRepository.findAllByIsCompletedTrue();
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Iterable<Task> findAllByOrderByCreateDateDesc() {
        return taskRepository.findAllByOrderByCreateDateDesc();
    }

    public Task save(@Valid Task task) {
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return taskRepository.save(task);
    }

    public void delete(Long taskId) {
        taskRepository.findById(taskId).ifPresentOrElse(taskRepository::delete, () -> {
            throw new EntityNotFoundException();
        });
    }

}
