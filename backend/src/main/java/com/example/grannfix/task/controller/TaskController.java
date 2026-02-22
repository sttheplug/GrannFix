package com.example.grannfix.task.controller;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.dto.UpdateTaskRequest;
import com.example.grannfix.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody CreateTaskRequest req) {

        TaskResponse savedTask = taskService.addTask(UUID.fromString(userId), req);
        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.id()))
                .body(savedTask);
    }

    @GetMapping("/me")
    public List<TaskResponse> getMyTasks(@AuthenticationPrincipal String userId) {
        return taskService.getMyTasks(UUID.fromString(userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateMyTask(
            @AuthenticationPrincipal String userId,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateTaskRequest req
    ) {
        return ResponseEntity.ok(taskService.updateMyTask(
                UUID.fromString(userId), id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal String userId,
            @PathVariable UUID id
    ) {
        taskService.deleteMyTask(UUID.fromString(userId), id);
        return ResponseEntity.noContent().build();
    }
}
