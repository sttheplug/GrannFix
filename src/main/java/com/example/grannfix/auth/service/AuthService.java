package com.example.grannfix.auth.service;

import com.example.grannfix.auth.dto.VerifyOtpResponse;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.model.User;
import com.example.grannfix.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public void sendOtp(String phoneNumber) {
        // TODO:
        // 1. Generate 6-digit code
        // 2. Save in DB or cache
        // 3. Send SMS
        System.out.println("OTP sent to " + phoneNumber);
    }

    public VerifyOtpResponse verifyOtp(String phoneNumber, String code) {

        // TODO:
        // 1. Validate OTP
        // 2. If valid -> find or create user

        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseGet(() -> createUser(phoneNumber));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        MeUserDto userDto = new MeUserDto(
                user.getId(),
                user.getPhoneNumber(),
                user.getName(),
                user.getBio(),
                user.getArea(),
                user.getStreet(),
                user.isVerified()
        );

        return new VerifyOtpResponse(
                accessToken,
                refreshToken,
                userDto
        );
    }

    private User createUser(String phoneNumber) {
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .name("New User")
                .area("Unknown")
                .build();

        return userRepository.save(user);
    }
}
