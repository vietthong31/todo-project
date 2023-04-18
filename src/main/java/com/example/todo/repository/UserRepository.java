package com.example.todo.repository;

import com.example.todo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUsername(String username);
}
