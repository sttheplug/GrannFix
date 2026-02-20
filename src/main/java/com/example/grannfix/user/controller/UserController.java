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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public MeUserDto getMe(@AuthenticationPrincipal String userId) {
        return userService.getMe(UUID.fromString(userId));
    }

    @PutMapping("/me")
    public MeUserDto updateMe(@AuthenticationPrincipal String userId,
                              @Valid @RequestBody UpdateMeRequest req) {
        return userService.updateMe(UUID.fromString(userId), req);
    }

    @GetMapping("/{id}")
    public PublicUserDto getPublicUser(@PathVariable UUID id) {
        return userService.getPublicUser(id);
    }
}
