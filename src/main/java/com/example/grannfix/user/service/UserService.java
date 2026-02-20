package com.example.grannfix.user.service;

import com.example.grannfix.user.repository.UserRepository;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import com.example.grannfix.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional(readOnly = true)
    public MeUserDto getMe(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        return mapToMeDto(u);
    }

    @Transactional
    public MeUserDto updateMe(UUID userId, UpdateMeRequest req) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        if (req.name() != null) u.setName(req.name());
        if (req.bio() != null) u.setBio(req.bio());
        if (req.street() != null) u.setStreet(req.street());
        updateAreaIfChanged(u, req.area());

        if (req.email() != null && !req.email().isBlank()) {
            String newEmail = req.email().trim().toLowerCase();

            if (u.getEmail() == null || !newEmail.equalsIgnoreCase(u.getEmail())) {
                if (userRepository.existsByEmail(newEmail)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
                u.setEmail(newEmail);
            }
        }
        userRepository.save(u);
        return mapToMeDto(u);
    }

    @Transactional
    public void removeMe(UUID userId){
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        u.setActive(false);
    }

    @Transactional(readOnly = true)
    public PublicUserDto getPublicUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is disabled");
        }
        return mapToPublicDto(u);
    }
    @Transactional(readOnly = true)
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::mapToAdminDto);
    }

    @Transactional
    public void removeUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        u.setActive(false);
        userRepository.save(u);
    }

    private void updateAreaIfChanged(User user, String requestedArea) {
        if (requestedArea == null || requestedArea.isBlank()) {
            return;
        }
        String newArea = requestedArea.trim();
        if (user.getArea() != null &&
                user.getArea().equalsIgnoreCase(newArea)) {
            return;
        }
        if (user.getAreaUpdatedAt() != null) {
            Instant nextAllowed = user.getAreaUpdatedAt()
                    .plus(Duration.ofDays(7));

            if (Instant.now().isBefore(nextAllowed)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "AREA_CHANGE_COOLDOWN"
                );
            }
        }
        user.setArea(newArea);
        user.setAreaUpdatedAt(Instant.now());
    }

    private MeUserDto mapToMeDto(User u) {
        return new MeUserDto(
                u.getId(),
                u.getPhoneNumber(),
                u.getEmail(),
                u.getName(),
                u.getBio(),
                u.getArea(),
                u.getStreet(),
                u.isVerified()
        );
    }

    private PublicUserDto mapToPublicDto(User u) {
        return new PublicUserDto(
                u.getId(),
                u.getName(),
                u.getBio(),
                u.getArea(),
                u.getRatingAverage() != null ? u.getRatingAverage() : 0.0,
                u.getRatingCount() != null ? u.getRatingCount() : 0,
                u.getPhoneNumber()
        );
    }

    private AdminUserDto mapToAdminDto(User u) {
        return new AdminUserDto(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getPhoneNumber(),
                u.getArea(),
                u.isActive(),
                u.isVerified(),
                u.getRole(),
                u.getRatingAverage() != null ? u.getRatingAverage() : 0.0,
                u.getRatingCount() != null ? u.getRatingCount() : 0,
                u.getCreatedAt()
        );
    }
}
