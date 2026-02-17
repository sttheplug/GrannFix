package com.example.grannfix.auth.controller;

import com.example.grannfix.auth.dto.*;
import com.example.grannfix.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        authService.sendOtp(phoneNumber);
        return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(
            @RequestBody @Valid VerifyOtpRequest request) {

        AuthResponse response =
                authService.verifyOtp(request.phoneNumber(), request.code());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequest request) {

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
