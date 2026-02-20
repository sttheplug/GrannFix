package com.example.grannfix.auth.dto;
import jakarta.validation.constraints.NotBlank;
public record SendOtpRequest(
        @NotBlank String phoneNumber
) {}
