package com.example.grannfix.auth.application.ports.out;

public record CreateUserCommand(
        String email,
        String phoneNumber,
        String encodedPassword,
        String name,
        String city,
        String area
) {}