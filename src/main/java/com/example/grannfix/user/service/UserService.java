package com.example.grannfix.user.service;
import com.example.grannfix.user.UserRepository;
import com.example.grannfix.user.dto.AdminUserDto;
import com.example.grannfix.user.dto.MeUserDto;
import com.example.grannfix.user.dto.PublicUserDto;
import com.example.grannfix.user.dto.UpdateMeRequest;
import com.example.grannfix.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
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
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!u.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User disabled");
        }
        return new MeUserDto(
                u.getId(),
                u.getPhoneNumber(),
                u.getName(),
                u.getBio(),
                u.getArea(),
                u.getStreet(),
                u.isVerified()
        );
    }
    @Transactional
    public MeUserDto updateMe(UUID userId, UpdateMeRequest req) {
        User u = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        u.setName(req.name());
        u.setBio(req.bio());
        u.setArea(req.area());
        u.setStreet(req.street());
        userRepository.save(u);
        return new MeUserDto(
                u.getId(),
                u.getPhoneNumber(),
                u.getName(),
                u.getBio(),
                u.getArea(),
                u.getStreet(),
                u.isVerified()
        );
    }
    @Transactional(readOnly = true)
    public PublicUserDto getPublicUser(UUID id) {
        User u = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new PublicUserDto(
                u.getId(),
                u.getName(),
                u.getBio(),
                u.getArea(),
                u.getRatingAverage() != null ? u.getRatingAverage() : 0.0,
                u.getRatingCount() != null ? u.getRatingCount() : 0
        );
    }
    @Transactional(readOnly = true)
    public Page<AdminUserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(u -> new AdminUserDto(
                u.getId(),
                u.getName(),
                u.getArea(),
                u.isActive(),
                u.isVerified(),
                u.getRole(),
                u.getRatingAverage(),
                u.getRatingCount(),
                u.getCreatedAt()
        ));
    }

    @Transactional
    public void removeUser(UUID userId) {
        userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.deleteById(userId);
    }
}
