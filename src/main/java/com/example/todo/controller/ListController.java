package com.example.todo.controller;

import com.example.todo.entity.List;
import com.example.todo.entity.User;
import com.example.todo.repository.ListRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lists")
public class ListController {
    private final ListRepository listRepository;

    public ListController(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @GetMapping("")
    public Iterable<List> lists() {
        return listRepository.findAll();
    }

    @GetMapping("/{listId}")
    public ResponseEntity<List> listById(@PathVariable Long listId) {
        Optional<List> list = listRepository.findById(listId);
        return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public List saveList(@RequestParam String title, @AuthenticationPrincipal User user) {
        List list = new List(title.isEmpty() ? "Untitled" : title);
        list.setUser(user);
        return listRepository.save(list);
    }

    @PutMapping("/{listId}")
    public void updateList(@PathVariable Long listId,
                           @RequestParam String title,
                           @AuthenticationPrincipal User user,
                           HttpServletResponse response) {
        listRepository.findById(listId)
                .ifPresentOrElse(list -> {
                    list.setTitle(title);
                    listRepository.save(list);
                    response.setStatus(HttpStatus.OK.value());
                }, () -> {
                    List list = new List(title);
                    list.setUser(user);
                    response.setStatus(HttpStatus.CREATED.value());
                });
    }

    @DeleteMapping("/{listId}")
    public Map<String, String> deleteTask(@PathVariable Long listId, HttpServletResponse response) {
        Map<String, String> msg = new HashMap<>();
        listRepository.findById(listId)
                .ifPresentOrElse(list -> {
                    listRepository.delete(list);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                    msg.put("message", "Deleted list " + listId);
                }, () -> {
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    msg.put("message", "List " + listId + " not found");
                });
        return msg;
    }

}
