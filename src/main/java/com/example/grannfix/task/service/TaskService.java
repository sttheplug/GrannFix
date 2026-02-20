package com.example.grannfix.task.service;

import com.example.grannfix.task.dto.CreateTaskRequest;
import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
}
