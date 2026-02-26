package com.example.grannfix.auth.application;

import com.example.grannfix.auth.api.dto.*;
import com.example.grannfix.auth.application.ports.out.CreateUserCommand;
import com.example.grannfix.auth.application.ports.out.UserAuthPort;
import com.example.grannfix.auth.application.ports.out.UserAuthView;
import com.example.grannfix.auth.domain.PasswordResetToken;
import com.example.grannfix.auth.infrastructure.messaging.email.EmailSender;
import com.example.grannfix.auth.infrastructure.messaging.sms.SmsSender;
import com.example.grannfix.auth.infrastructure.security.JwtService;
import com.example.grannfix.auth.persistence.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserAuthPort userAuthPort;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailSender emailSender;
    private final SmsSender smsSender;

    public void sendOtp(String phoneNumber) {
        phoneNumber = normalizePhone(phoneNumber);
        validatePhone(phoneNumber);
        userAuthPort.findByPhone(phoneNumber).ifPresent(user -> {
            if (!user.active()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
            }
            if (user.verified()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already verified");
            }
        });
        String code = otpService.generateAndStore(phoneNumber);
        System.out.println("Your GrannFix verification code is: " + code + " (valid for 5 minutes)");
        // smsSender.send(phoneNumber, "Your GrannFix verification code is: " + code);
    }

    public AuthResponse verifyOtp(String phoneNumber, String code) {
        String normalizedPhone = normalizePhone(phoneNumber);
        validatePhone(normalizedPhone);

        boolean ok = otpService.verify(normalizedPhone, code);
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired OTP");
        }

        UserAuthView user = userAuthPort.findByPhone(normalizedPhone)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.active()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        if (!user.verified()) {
            userAuthPort.markVerified(user.id());
        }
        return buildAuthResponse(user);
    }

    public AuthResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        String phone = normalizePhone(req.phoneNumber());
        validatePhone(phone);

        if (userAuthPort.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        CreateUserCommand cmd = new CreateUserCommand(
                email,
                phone,
                passwordEncoder.encode(req.password()),
                req.name(),
                req.city().trim(),
                req.area().trim()
        );

        UserAuthView user = userAuthPort.createUser(cmd);
        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();

        UserAuthView user = userAuthPort.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (user.passwordHash() == null || !passwordEncoder.matches(req.password(), user.passwordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        if (!user.active()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        if (!user.verified()) {
            userAuthPort.markVerified(user.id());
            user = userAuthPort.findById(user.id())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
        return buildAuthResponse(user);
    }

    public void forgotPassword(String emailRaw) {
        String email = emailRaw.trim().toLowerCase();

        userAuthPort.findByEmail(email).ifPresent(user -> {
            if (!user.active()) return;

            String token = generateSecureToken();
            Instant now = Instant.now();
            PasswordResetToken prt = PasswordResetToken.builder()
                    .token(token)
                    .userId(user.id())
                    .expiresAt(now.plus(Duration.ofMinutes(30)))
                    .used(false)
                    .build();

            passwordResetTokenRepository.save(prt);

            String body = """
                    Hej!

                    Här är din återställningskod (token):
                    %s

                    Använd den i /auth/reset-password tillsammans med ditt nya lösenord.
                    Token är giltig i 30 minuter. Om du inte begärde detta kan du ignorera mailet.
                    """.formatted(token);

            emailSender.send(user.email(), "Återställ lösenord – GrannFix", body);
        });
    }

    public void resetPassword(String tokenRaw, String newPassword) {
        String token = tokenRaw.trim();

        PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));

        if (prt.isUsed()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token already used");
        }
        if (Instant.now().isAfter(prt.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        }
        UserAuthView user = userAuthPort.findById(prt.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.active()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }

        userAuthPort.updatePassword(user.id(), passwordEncoder.encode(newPassword));
        prt.setUsed(true);
        passwordResetTokenRepository.save(prt);
    }

    private AuthResponse buildAuthResponse(UserAuthView user) {
        String accessToken = jwtService.generateAccessToken(user.id());
        String refreshToken = jwtService.generateRefreshToken(user.id());

        AuthUserDto userDto = new AuthUserDto(
                user.id(),
                user.phoneNumber(),
                user.email(),
                user.name(),
                user.bio(),
                user.city(),
                user.area(),
                user.street(),
                user.verified()
        );
        return new AuthResponse(accessToken, refreshToken, userDto);
    }

    private String generateSecureToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String normalizePhone(String phoneNumber) {
        if (phoneNumber == null) return null;
        String p = phoneNumber.replaceAll("[^0-9+]", "");

        if (p.startsWith("+")) return p;
        if (p.startsWith("00")) return "+" + p.substring(2);
        if (p.startsWith("46")) return "+" + p;
        if (p.startsWith("0")) return "+46" + p.substring(1);
        return p;
    }

    private void validatePhone(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("^\\+[1-9]\\d{7,14}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number");
        }
    }
}