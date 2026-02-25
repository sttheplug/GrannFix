package com.example.grannfix.user.service;

import com.example.grannfix.task.model.Task;
import com.example.grannfix.task.model.TaskStatus;
import com.example.grannfix.task.repository.TaskRepository;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.mapper.UserMapper;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toAdminDto);
    }

    public void reactivateUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (u.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already active");
        }
        u.setActive(true);
    }

    public void deactivateUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already inactive");
        }
        u.setActive(false);
        List<Task> tasks = taskRepository.findByCreatedBy_IdAndStatusIn(
                u.getId(), List.of(TaskStatus.OPEN, TaskStatus.ASSIGNED)
        );
        for (Task task : tasks) {
            task.setStatus(TaskStatus.CANCELLED);
        }
    }
}