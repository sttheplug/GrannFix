package com.example.grannfix.auth.api.dto;
import jakarta.validation.constraints.NotBlank;
public record SendOtpRequest(
        @NotBlank String phoneNumber
) {}
