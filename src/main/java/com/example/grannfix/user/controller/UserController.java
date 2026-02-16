package com.example.grannfix.user.controller;

import com.example.grannfix.user.service.UserService;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/me")
    public MeUserDto getMe(Authentication auth) {
        UUID userId = extractUserId(auth);
        return userService.getMe(userId);
    }

    @PutMapping("/users/me")
    public MeUserDto updateMe(Authentication auth, @Valid @RequestBody UpdateMeRequest req) {
        UUID userId = extractUserId(auth);
        return userService.updateMe(userId, req);
    }

    @GetMapping("/users/{id}")
    public PublicUserDto getPublicUser(@PathVariable UUID id) {
        return userService.getPublicUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/remove/{id}")
    public void removeUser(@PathVariable UUID id) {
        userService.removeUser(id);
    }

    private UUID extractUserId(Authentication auth) {
        return UUID.fromString(auth.getName());
    }
}
