package com.example.grannfix.auth.dto;
import com.example.grannfix.user.dto.MeUserDto;
public record VerifyOtpResponse(
        String accessToken,
        String refreshToken,
        MeUserDto user
) {}
