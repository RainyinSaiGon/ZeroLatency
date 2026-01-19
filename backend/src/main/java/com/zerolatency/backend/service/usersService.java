package com.zerolatency.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.model.AuthProvider;
import com.zerolatency.backend.model.users;
import com.zerolatency.backend.model.PasswordResetToken;
import com.zerolatency.backend.model.EmailVerificationToken;
import com.zerolatency.backend.repo.usersRepo;
import com.zerolatency.backend.repo.PasswordResetTokenRepo;
import com.zerolatency.backend.repo.EmailVerificationTokenRepo;
import com.zerolatency.backend.security.JwtUtil;
import com.zerolatency.backend.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class usersService {
    @Autowired
    private usersRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private EmailVerificationTokenRepo emailVerificationTokenRepo;

    @Autowired
    private EmailService emailService;

    public users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public users findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<users> getAllUsers() {
        return userRepo.findAll();
    }

    public users saveUser(users user) {
        return userRepo.save(user);
    }

    public boolean verifyPassword(String username, String password) {
        users user = userRepo.findByUsername(username);
        if (user != null && user.getPassword() != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public users login(String username, String password) {
        users user = userRepo.findByUsername(username);
        if (user != null && user.getPassword() != null &&
                passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public users register(String username, String email, String password) {
        // Check if username or email already exists
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        users user = new users();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        user.setProvider(AuthProvider.LOCAL);
        user.setEnabled(true);

        users savedUser = userRepo.save(user);

        // Send verification email
        sendVerificationEmail(savedUser);

        return savedUser;
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public Long getTokenExpiration() {
        return jwtUtil.getExpirationTime();
    }

    public String getUsernameFromToken(String token) {
        try {
            return jwtUtil.extractUsername(token);
        } catch (Exception e) {
            return null;
        }
    }

    public UserDTO toUserDTO(users user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getProvider() != null ? user.getProvider().name() : null,
                user.getProfilePicture());
    }

    // Password Reset Methods
    public void requestPasswordReset(String email) {
        users user = userRepo.findByEmail(email);
        if (user == null) {
            // Don't reveal if email exists or not for security
            return;
        }

        // Generate reset token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

        PasswordResetToken resetToken = new PasswordResetToken(token, user, expiresAt);
        passwordResetTokenRepo.save(resetToken);

        // Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));

        if (resetToken.getUsed()) {
            throw new RuntimeException("Reset token has already been used");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("Reset token has expired");
        }

        // Update password
        users user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        passwordResetTokenRepo.save(resetToken);
    }

    // Email Verification Methods
    public void sendVerificationEmail(users user) {
        // Generate verification token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user, expiresAt);
        emailVerificationTokenRepo.save(verificationToken);

        // Update user with token
        user.setEmailVerificationToken(token);
        userRepo.save(user);

        // Send email
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (verificationToken.isExpired()) {
            throw new RuntimeException("Verification token has expired");
        }

        // Mark user as verified
        users user = verificationToken.getUser();
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepo.save(user);

        // Delete the token
        emailVerificationTokenRepo.delete(verificationToken);
    }

    public void resendVerificationEmail(String email) {
        users user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        // Delete old verification tokens
        if (user.getEmailVerificationToken() != null) {
            emailVerificationTokenRepo.findByToken(user.getEmailVerificationToken())
                    .ifPresent(emailVerificationTokenRepo::delete);
        }

        // Send new verification email
        sendVerificationEmail(user);
    }
}
