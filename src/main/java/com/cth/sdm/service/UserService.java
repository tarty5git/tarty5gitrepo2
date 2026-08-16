package com.cth.sdm.service;

import com.cth.sdm.entity.User;
import com.cth.sdm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       NotificationService notificationService,
                       AuditService auditService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(String username, String rawPassword, String email, String mobileNumber, String role, String adminActor) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        User user = new User(username, passwordEncoder.encode(rawPassword), email, mobileNumber, role);
        User saved = userRepository.save(user);

        auditService.logEvent("USER_CREATED", adminActor, null, "Created user: " + username + " with role: " + role);
        notificationService.sendAccountCreatedNotification(username, email, mobileNumber);

        return saved;
    }

    @Transactional
    public void setUserLockStatus(String username, boolean lock, String adminActor) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.setLocked(lock);
        userRepository.save(user);

        String action = lock ? "LOCKED" : "UNLOCKED";
        auditService.logEvent("USER_LOCK_TOGGLE", adminActor, null, "User " + username + " " + action);
    }

    @Transactional
    public void resetPassword(String username, String newRawPassword, String adminActor) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.setPassword(passwordEncoder.encode(newRawPassword));
        user.setPasswordUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        auditService.logEvent("PASSWORD_RESET", adminActor, null, "Reset password for user: " + username);
    }

    public boolean isPasswordExpired(User user, int expirationDays) {
        if (user.getPasswordUpdatedAt() == null) {
            return false;
        }
        return user.getPasswordUpdatedAt().plusDays(expirationDays).isBefore(LocalDateTime.now());
    }
}
