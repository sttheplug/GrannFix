package com.example.grannfix.user.controller;

import com.example.grannfix.user.service.UserService;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public MeUserDto getMe(@AuthenticationPrincipal UUID userId) {
        return userService.getMe(userId);
    }

    @PatchMapping("/me")
    public MeUserDto updateMe(@AuthenticationPrincipal UUID userId,
                              @Valid @RequestBody UpdateMeRequest req) {
        return userService.updateMe(userId, req);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> removeMe(@AuthenticationPrincipal UUID userId) {
        userService.removeMe(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public PublicUserDto getPublicUser(@PathVariable UUID id) {
        return userService.getPublicUser(id);
    }
}