package com.example.grannfix.user.service;

import com.example.grannfix.user.repository.UserRepository;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import com.example.grannfix.user.model.User;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        updateLocationIfChanged(u, req.city(), req.area());

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
        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already inactive");
        }
        u.setActive(false);
    }

    @Transactional
    public void reactivateUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (u.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already active");
        }
        u.setActive(true);
    }

    private void updateLocationIfChanged(User user, String requestedCity, String requestedArea) {

        if (requestedCity == null || requestedCity.isBlank() ||
                requestedArea == null || requestedArea.isBlank()) {
            return;
        }
        String newCity = requestedCity.trim();
        String newArea = requestedArea.trim();

        boolean sameLocation =
                newCity.equalsIgnoreCase(user.getCity()) &&
                        newArea.equalsIgnoreCase(user.getArea());

        if (sameLocation) {
            return;
        }
        if (user.getAreaUpdatedAt() != null) {
            Instant nextAllowed = user.getAreaUpdatedAt()
                    .plus(Duration.ofDays(7));

            if (Instant.now().isBefore(nextAllowed)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "LOCATION_CHANGE_COOLDOWN"
                );
            }
        }
        user.setCity(newCity);
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
                u.getCity(),
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
                u.getCity(),
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
                u.getCity(),
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
