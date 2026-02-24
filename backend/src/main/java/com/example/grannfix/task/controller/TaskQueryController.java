package com.example.grannfix.task.controller;

import com.example.grannfix.task.dto.CursorPageResponse;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.model.TaskStatus;
import com.example.grannfix.task.service.TaskQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskQueryController {

    private final TaskQueryService taskQueryService;

    @GetMapping
    public CursorPageResponse<TaskResponse> listTasks(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area
    ) {
        return taskQueryService.listTasks(cursor, limit, status, city, area);
    }
}