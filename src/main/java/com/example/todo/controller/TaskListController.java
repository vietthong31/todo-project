package com.example.todo.controller;

import com.example.todo.entity.List;
import com.example.todo.repository.ListRepository;
import com.example.todo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class TaskListController {

    private final TaskService taskService;
    private final ListRepository listRepository;

    public TaskListController(TaskService taskService, ListRepository listRepository) {
        this.taskService = taskService;
        this.listRepository = listRepository;
    }

    @ModelAttribute("lists")
    Iterable<List> lists() {
        return listRepository.findAllByCurrentUser();
    }

    @GetMapping({"/today"})
    public String todayTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllTodayTasks());
        return "task";
    }

    @GetMapping("/task/{taskId}/edit")
    public String taskEditForm(@PathVariable Long taskId, Model model) {
        return taskService
                .findById(taskId)
                .map(task -> {
                    model.addAttribute("task", task);
                    return "edit_task";
                })
                .orElseGet(() -> "task");
    }

    @GetMapping("/{listId}")
    public String listOfTasks(@PathVariable Long listId, Model model) {
        if (!listRepository.existsById(listId)) {
            return "redirect:/manage/today";
        }
        model.addAttribute("listId", listId);
        model.addAttribute("tasks", taskService.findAllByListId(listId));
        return "task";
    }
}
