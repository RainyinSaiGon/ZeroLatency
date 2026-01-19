package com.zerolatency.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerolatency.backend.model.AuthProvider;
import com.zerolatency.backend.model.users;
import com.zerolatency.backend.repo.usersRepo;
import com.zerolatency.backend.security.JwtUtil;
import com.zerolatency.backend.dto.UserDTO;

import java.util.List;

@Service
public class usersService {
    @Autowired
    private usersRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public users findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<users> getAllUsers() {
        return userRepo.findAll();
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

        return userRepo.save(user);
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
}
