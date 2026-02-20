package com.example.grannfix.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public record ForgotPasswordRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email
) {}
