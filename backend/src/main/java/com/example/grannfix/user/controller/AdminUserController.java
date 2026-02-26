package com.example.grannfix.user.controller;

import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.service.AdminUserService;
import com.example.grannfix.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping("/{id}:reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable UUID id) {
        adminUserService.reactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return adminUserService.getAllUsers(pageable);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        adminUserService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}
