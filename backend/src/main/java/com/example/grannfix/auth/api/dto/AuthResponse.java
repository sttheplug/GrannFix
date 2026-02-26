package com.example.grannfix.auth.api.dto;
public record AuthResponse(
        String accessToken,
        String refreshToken,
        AuthUserDto user
) {}
