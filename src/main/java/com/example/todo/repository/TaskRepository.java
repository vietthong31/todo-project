package com.example.todo.repository;

import com.example.todo.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user.id = ?#{principal?.id}")
    Iterable<Task> findAllByUserId();

    @Query("SELECT t FROM TaskList tl INNER JOIN tl.task t WHERE tl.list.id = :listId")
    Iterable<Task> findAllByListId(Long listId);

    Iterable<Task> findAllByIsCompletedTrue();
    Iterable<Task> findAllByIsCompletedFalse();

    Iterable<Task> findAllByOrderByCreateDateDesc();


}
