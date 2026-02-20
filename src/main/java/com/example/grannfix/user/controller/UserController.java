package com.example.grannfix.user.controller;

import com.example.grannfix.user.service.UserService;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public MeUserDto getMe(@AuthenticationPrincipal String userId) {
        return userService.getMe(UUID.fromString(userId));
    }

    @PutMapping("/users/me")
    public MeUserDto updateMe(@AuthenticationPrincipal String userId,
                              @Valid @RequestBody UpdateMeRequest req) {
        return userService.updateMe(UUID.fromString(userId), req);
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
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable UUID id) {
        userService.removeUser(id);
        return ResponseEntity.noContent().build();
    }


}
