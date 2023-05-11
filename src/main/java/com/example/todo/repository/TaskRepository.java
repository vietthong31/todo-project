package com.example.todo.repository;

import com.example.todo.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Iterable<Task> findAllByListId(Long lisId);

    Iterable<Task> findAllByIsCompletedTrue();
    Iterable<Task> findAllByIsCompletedFalse();

    Iterable<Task> findAllByOrderByCreateDateDesc();

    Iterable<Task> findAllByDueDateBetween(LocalDateTime dueDate, LocalDateTime dueDate2);


}
