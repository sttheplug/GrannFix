package com.example.grannfix.task.controller;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public ResponseEntity<Task> createTask(Authentication authentication,
            @Valid @RequestBody CreateTaskRequest req) {

        UUID userId = UUID.fromString(authentication.getName());
        Task savedTask = taskService.addTask(userId, req);
        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.getId()))
                .body(savedTask);
    }
}
