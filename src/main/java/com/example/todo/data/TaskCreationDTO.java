package com.example.todo.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskCreationDTO(@NotNull @NotBlank String content,
                              @NotBlank Long listId,
                              @NotNull LocalDateTime dueDate) {
}
