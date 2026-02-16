package com.example.grannfix.user.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMeRequest(
        @NotBlank @Size(max = 80) String name,
        @Size(max = 500) String bio,
        @NotBlank @Size(max = 80) String area,
        @Size(max = 120) String street
) {}
