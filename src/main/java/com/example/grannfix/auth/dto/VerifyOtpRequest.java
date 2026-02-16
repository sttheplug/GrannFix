package com.example.grannfix.auth.dto;
import jakarta.validation.constraints.NotBlank;
public record VerifyOtpRequest(
        @NotBlank String phoneNumber,
        @NotBlank String code
) {}
