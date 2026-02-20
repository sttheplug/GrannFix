package com.example.grannfix.user.controller;

import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable UUID id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }
}
