package com.example.grannfix.auth.service;

import com.example.grannfix.auth.dto.LoginRequest;
import com.example.grannfix.auth.dto.RegisterRequest;
import com.example.grannfix.auth.dto.AuthResponse;
import com.example.grannfix.auth.security.JwtService;
import com.example.grannfix.auth.sms.SmsSender;
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
    private final OtpService otpService;
    private final SmsSender smsSender;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder, OtpService otpService, SmsSender smsSender) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.smsSender = smsSender;
    }

    public void sendOtp(String phoneNumber) {
        phoneNumber = normalizePhone(phoneNumber);
        validatePhone(phoneNumber);
        String code = otpService.generateAndStore(phoneNumber);
        System.out.println("Your GrannFix verification code is: " + code + " (valid for 5 minutes)");
        //smsSender.send(phoneNumber, message);
    }

    public AuthResponse verifyOtp(String phoneNumber, String code) {
        String normalizedPhone = normalizePhone(phoneNumber);
        validatePhone(normalizedPhone);

        boolean ok = otpService.verify(normalizedPhone, code);
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired OTP");
        }
        User user = userRepository.findByPhoneNumber(normalizedPhone)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        if (!user.isVerified()) {
            user.setVerified(true);
            userRepository.save(user);
        }
        return buildAuthResponse(user);
    }

    public AuthResponse register(RegisterRequest req) {
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
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (user.getPassword() == null || !passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
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
        return new AuthResponse(accessToken, refreshToken, userDto);
    }
    private String normalizePhone(String phoneNumber) {
        if (phoneNumber == null) return null;
        String p = phoneNumber.replaceAll("[^0-9+]", "");

        if (p.startsWith("+")) {
            return p;
        }
        if (p.startsWith("00")) {
            return "+" + p.substring(2);
        }
        if (p.startsWith("46")) {
            return "+" + p;
        }
        if (p.startsWith("0")) {
            return "+46" + p.substring(1);
        }
        return p;
    }


    private void validatePhone(String phoneNumber) {
        if (!phoneNumber.matches("^\\+[1-9]\\d{7,14}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number");
        }
    }

}
