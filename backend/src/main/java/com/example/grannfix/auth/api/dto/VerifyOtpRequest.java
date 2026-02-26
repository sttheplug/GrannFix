package com.example.grannfix.auth.api.dto;
import jakarta.validation.constraints.NotBlank;
public record VerifyOtpRequest(
        @NotBlank String phoneNumber,
        @NotBlank String code
) {}
