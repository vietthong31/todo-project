package com.example.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoApplication {

    //UserRepository repository;

    //TodoApplication(UserRepository repository) {
    //    this.repository = repository;
    //}

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }


    //@Override
    //public void run(String... args) throws Exception {
    //    System.out.println("Running");
    //    System.out.println(repository.findByUsername("xyz").getUsername());
    //}
}
