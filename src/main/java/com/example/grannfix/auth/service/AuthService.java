package com.example.grannfix.auth.service;

import com.example.grannfix.auth.dto.LoginRequest;
import com.example.grannfix.auth.dto.RegisterRequest;
import com.example.grannfix.auth.dto.VerifyOtpResponse;
import com.example.grannfix.auth.security.JwtService;
import com.example.grannfix.user.repository.UserRepository;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendOtp(String phoneNumber) {
        // TODO: generate OTP + store + send SMS
        System.out.println("OTP sent to " + phoneNumber);
    }

    public VerifyOtpResponse verifyOtp(String phoneNumber, String code) {
        // TODO: validate OTP properly (currently missing)
        User user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseGet(() -> createUserWithPhone(phoneNumber));

        return tokensResponse(user);
    }

    private User createUserWithPhone(String phoneNumber) {
        User user = User.builder()
                .phoneNumber(phoneNumber)
                .name("New User")
                .area("Unknown")
                .active(true)
                .verified(false)
                .build();
        return userRepository.save(user);
    }

    public VerifyOtpResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        User user = User.builder()
                .email(email)
                .phoneNumber(req.phoneNumber())
                .password(passwordEncoder.encode(req.password()))
                .name(req.name())
                .area("Unknown")
                .active(true)
                .verified(false)
                .build();

        userRepository.save(user);
        return tokensResponse(user);
    }

    public VerifyOtpResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (user.getPassword() == null || !passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        return tokensResponse(user);
    }

    private VerifyOtpResponse tokensResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        MeUserDto userDto = new MeUserDto(
                user.getId(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getName(),
                user.getBio(),
                user.getArea(),
                user.getStreet(),
                user.isVerified()
        );

        return new VerifyOtpResponse(accessToken, refreshToken, userDto);
    }
}
