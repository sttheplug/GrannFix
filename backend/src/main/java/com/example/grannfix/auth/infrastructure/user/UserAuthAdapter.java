package com.example.grannfix.auth.infrastructure.user;

import com.example.grannfix.auth.application.ports.out.CreateUserCommand;
import com.example.grannfix.auth.application.ports.out.UserAuthPort;
import com.example.grannfix.auth.application.ports.out.UserAuthView;
import com.example.grannfix.user.domain.User;
import com.example.grannfix.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAuthAdapter implements UserAuthPort {

    private final UserRepository userRepository;

    @Override
    public Optional<UserAuthView> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toView);
    }

    @Override
    public Optional<UserAuthView> findByPhone(String phone) {
        return userRepository.findByPhoneNumber(phone).map(this::toView);
    }

    @Override
    public Optional<UserAuthView> findById(UUID userId) {
        return userRepository.findById(userId).map(this::toView);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserAuthView createUser(CreateUserCommand cmd) {
        User user = User.builder()
                .email(cmd.email())
                .phoneNumber(cmd.phoneNumber())
                .password(cmd.encodedPassword())
                .name(cmd.name())
                .city(cmd.city())
                .area(cmd.area())
                .active(true)
                .verified(false)
                .build();
        userRepository.save(user);
        return toView(user);
    }

    @Override
    public void markVerified(UUID userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setVerified(true);
            userRepository.save(u);
        });
    }

    @Override
    public void updatePassword(UUID userId, String encodedPassword) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setPassword(encodedPassword);
            userRepository.save(u);
        });
    }

    private UserAuthView toView(User u) {
        return new UserAuthView(
                u.getId(),
                u.getPhoneNumber(),
                u.getEmail(),
                u.getPassword(),
                u.getName(),
                u.getBio(),
                u.getCity(),
                u.getArea(),
                u.getStreet(),
                u.isActive(),
                u.isVerified()
        );
    }
}