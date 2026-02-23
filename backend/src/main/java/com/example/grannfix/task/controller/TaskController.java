package com.example.grannfix.task.controller;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskDetailResponse;
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
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody CreateTaskRequest req) {

        TaskResponse savedTask = taskService.addTask(userId, req);
        return ResponseEntity
                .created(URI.create("/tasks/" + savedTask.id()))
                .body(savedTask);
    }

    @GetMapping("/me")
    public List<TaskResponse> getMyTasks(@AuthenticationPrincipal UUID userId) {
        return taskService.getMyTasks(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetailResponse> getTask(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                taskService.getTaskById(userId, id)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateMyTask(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID id,
            @RequestBody @Valid UpdateTaskRequest req
    ) {
        return ResponseEntity.ok(taskService.updateMyTask(
                userId, id, req));
    }

    @PostMapping("/{id}:cancel")
    public ResponseEntity<Void> cancelTask(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID id
    ){
        taskService.cancelMyTask(userId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UUID userId,
            @PathVariable UUID id
    ) {
        taskService.deleteMyTask(userId, id);
        return ResponseEntity.noContent().build();
    }
}