package com.example.todo.controller;

import com.example.todo.data.TaskDTO;
import com.example.todo.data.TaskMapper;
import com.example.todo.entity.Task;
import com.example.todo.security.UserDetailsImpl;
import com.example.todo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper mapper;

    public TaskController(TaskService taskService, TaskMapper mapper) {
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String task(Model model) {
        model.addAttribute("tasks", taskService.findAllByOrderByCreateDateDesc());
        return "task";
    }

    @GetMapping("/edit/{taskId}")
    public String editTaskForm(@PathVariable("taskId") Long taskId, Model model) {
        Optional<Task> task = taskService.findById(taskId);
        if (task.isPresent()) {
            model.addAttribute("task", task.get());
            return "edit_task";
        } else {
            return "task";
        }
    }

    @PostMapping("/edit")
    public String updateTask(TaskDTO taskDTO) {
        taskService.findById(taskDTO.getId()).ifPresent(task -> {
            task.setContent(taskDTO.getContent());
            task.setDueDate(taskDTO.getDueDate());
            task.setIsCompleted(taskDTO.getIsCompleted());
            taskService.save(task);
        });
        return "redirect:/task";
    }

    @PostMapping("/edit/completed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompletedStatus(@RequestBody TaskDTO taskDTO) {
        taskService.findById(taskDTO.getId()).ifPresent(task -> {
            task.setIsCompleted(taskDTO.getIsCompleted());
            taskService.save(task);
        });
    }

    @GetMapping("/completed")
    public String doneTask(Model model) {
        model.addAttribute("tasks", taskService.completedTasks());
        model.addAttribute("hideForm", true);
        return "task";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TaskDTO create(TaskDTO taskDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Task task = mapper.toTask(taskDTO);
        task.setUser(userDetails.getUser());
        task.setIsCompleted(false);
        task.setCreateDate(LocalDate.now());
        return mapper.toTaskDTO(taskService.save(task));
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("taskId") Long taskId) {
        taskService.delete(taskId);
    }
}
