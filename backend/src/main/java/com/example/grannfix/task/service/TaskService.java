package com.example.grannfix.task.service;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.dto.TaskResponse;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public Task addTask(UUID createdById, CreateTaskRequest req) {
        User createdBy = userRepository.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + createdById));

        if (!createdBy.isVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Phone number must be verified (OTP) before creating tasks."
            );
        }
        BigDecimal price = req.offeredPrice();
        if (price != null && price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("offeredPrice cannot be negative");
        }

        Task task = Task.builder()
                .createdBy(createdBy)
                .title(req.title().trim())
                .description(req.description().trim())
                .city(req.city().trim())
                .area(req.area().trim())
                .street(req.street() != null ? req.street().trim() : null)
                .offeredPrice(price)
                .build();
        return taskRepository.save(task);
    }
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(UUID userId) {
        if (!userRepository.existsByIdAndActiveTrue(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        return taskRepository.findByCreatedBy_Id(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TaskResponse toResponse(Task t) {
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
