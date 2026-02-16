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

    // ---------------- OTP ----------------

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phoneNumber) {
        authService.sendOtp(phoneNumber);
        return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<VerifyOtpResponse> verifyOtp(
            @RequestBody @Valid VerifyOtpRequest request) {

        VerifyOtpResponse response =
                authService.verifyOtp(request.phoneNumber(), request.code());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<VerifyOtpResponse> register(
            @RequestBody @Valid RegisterRequest request) {

        VerifyOtpResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<VerifyOtpResponse> login(
            @RequestBody @Valid LoginRequest request) {

        VerifyOtpResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
