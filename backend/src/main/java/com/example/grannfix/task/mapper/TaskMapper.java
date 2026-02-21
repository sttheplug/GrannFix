package com.example.grannfix.task.mapper;

import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getCity(),
                t.getArea(),
                t.getStreet(),
                t.getOfferedPrice(),
                t.getStatus(),
                t.isActive(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getCompletedAt()
        );
    }
}