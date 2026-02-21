package com.example.grannfix.task.controller;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateTaskRequest req) {

        Task savedTask = taskService.addTask(UUID.fromString(userId), req);
        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.getId()))
                .body(savedTask);
    }

    @GetMapping("/me")
    public List<TaskResponse> getMyTasks(@AuthenticationPrincipal String userId) {
        return taskService.getMyTasks(UUID.fromString(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal String userId,
            @PathVariable UUID id
    ) {
        taskService.deleteMyTask(UUID.fromString(userId), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<TaskResponse> getTasks(
            @RequestParam(required = false) Instant cursor,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return taskService.getTasks(cursor, limit);
    }
}
