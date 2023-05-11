package com.example.todo.service;

import com.example.todo.entity.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Iterable<Task> completedTasks() {
        return taskRepository.findAllByIsCompletedTrue();
    }

    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    public Iterable<Task> findAll() {
        return taskRepository.findAll();
    }

    public Iterable<Task> findAllByListId(Long listId) {
        return taskRepository.findAllByListId(listId);
    }

    public Iterable<Task> findAllByDueDateBetween(LocalDateTime dueDate1, LocalDateTime dueDate2) {
        return taskRepository.findAllByDueDateBetween(dueDate1, dueDate2);
    }

    public Iterable<Task> findAllTodayTasks() {
        LocalDateTime datetime1 = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime dateTime2 = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return taskRepository.findAllByDueDateBetween(datetime1, dateTime2);
    }

    public Iterable<Task> findAllByOrderByCreateDateDesc() {
        return taskRepository.findAllByOrderByCreateDateDesc();
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
