package com.example.grannfix.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 6, max = 128) String password,
        @NotBlank @Size(max = 80) String name,
        @NotBlank String phoneNumber,
        @NotBlank @Size(max = 80) String city,
        @NotBlank @Size(max = 80) String area

) {}
