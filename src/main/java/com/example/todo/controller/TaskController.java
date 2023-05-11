package com.example.todo.controller;

import com.example.todo.data.TaskCreationDTO;
import com.example.todo.data.TaskEditionDTO;
import com.example.todo.entity.List;
import com.example.todo.entity.Task;
import com.example.todo.repository.ListRepository;
import com.example.todo.service.TaskService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ListRepository listRepository;

    public TaskController(TaskService taskService, ListRepository listRepository) {
        this.taskService = taskService;
        this.listRepository = listRepository;
    }

    @GetMapping("")
    public Iterable<Task> tasks() {
        return taskService.findAll();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> taskById(@PathVariable Long taskId) {
        return taskService
                .findById(taskId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public ResponseEntity<Task> saveTask(TaskCreationDTO dto) {
        Optional<List> list = listRepository.findById(dto.listId());
        return list.map(l -> {
            Task task = new Task(l, dto.content(), dto.dueDate(), false);
            task.setCreateDate(LocalDateTime.now());
            Task savedTask = taskService.save(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/{taskId}")
    public void updateTask(@PathVariable Long taskId, TaskEditionDTO dto, HttpServletResponse response) {
        Optional<List> list = listRepository.findById(dto.listId());
        if (list.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            taskService.findById(taskId)
                       .ifPresentOrElse(task -> {
                           task.setList(list.get());
                           task.setContent(dto.content());
                           task.setDueDate(dto.dueDate());
                           task.setIsCompleted(dto.isCompleted());
                           task.setUpdateDate(LocalDateTime.now());
                           taskService.save(task);
                           response.setStatus(HttpStatus.OK.value());
                       }, () -> {
                           Task newTask = taskService.save(new Task(list.get(),
                                   dto.content(),
                                   dto.dueDate(),
                                   dto.isCompleted()));
                           response.setStatus(HttpStatus.CREATED.value());
                           response.setHeader("Content-Location", "/tasks/" + newTask.getId());
                       });
        }
    }

    @PutMapping("/completed/{taskId}")
    public void updateTaskCompleted(@PathVariable Long taskId,
                                    @RequestParam boolean isCompleted,
                                    HttpServletResponse response) {
        taskService.findById(taskId)
                   .ifPresentOrElse(task -> {
                       task.setIsCompleted(isCompleted);
                       taskService.save(task);
                       response.setStatus(HttpStatus.OK.value());
                   }, () -> {
                       response.setStatus(HttpStatus.BAD_REQUEST.value());
                   });
    }

    @DeleteMapping("/{taskId}")
    public Map<String, String> deleteTask(@PathVariable Long taskId, HttpServletResponse response) {
        Map<String, String> msg = new HashMap<>();
        taskService.findById(taskId)
                .ifPresentOrElse(task -> {
                    taskService.delete(task);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                    msg.put("message", "Deleted task " + taskId);
                }, () -> {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    msg.put("message", "Task " + taskId + " not found");
                });
        return msg;
    }
}
