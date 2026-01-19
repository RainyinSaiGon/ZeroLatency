package com.zerolatency.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zerolatency.backend.dto.RegisterRequest;
import com.zerolatency.backend.dto.authResponse;
import com.zerolatency.backend.dto.loginRequest;
import com.zerolatency.backend.dto.usernameRequest;
import com.zerolatency.backend.dto.UserDTO;
import com.zerolatency.backend.model.users;
import com.zerolatency.backend.service.usersService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class usersController {
    @Autowired
    private usersService userService;

    @GetMapping
    public UserDTO findByUsername(@RequestParam String username) {
        users user = userService.findByUsername(username);
        return userService.toUserDTO(user);
    }

    @PostMapping
    public UserDTO findByUsernameFromBody(@RequestBody usernameRequest user) {
        users foundUser = userService.findByUsername(user.getUsername());
        return userService.toUserDTO(foundUser);
    }

    @GetMapping("/dashboard")
    public List<UserDTO> getDashboard() {
        return userService.getAllUsers().stream()
                .map(user -> userService.toUserDTO(user))
                .collect(java.util.stream.Collectors.toList());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody loginRequest request) {
        try {
            users user = userService.login(request.getUsername(), request.getPassword());
            if (user != null) {
                // Generate JWT token
                String token = userService.generateToken(user.getUsername());
                Long expiresAt = System.currentTimeMillis() + userService.getTokenExpiration();

                authResponse response = new authResponse(token, expiresAt, userService.toUserDTO(user));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred during login"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            users user = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword());

            // Generate JWT token for immediate login
            String token = userService.generateToken(user.getUsername());
            Long expiresAt = System.currentTimeMillis() + userService.getTokenExpiration();

            authResponse response = new authResponse(token, expiresAt, userService.toUserDTO(user));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred during registration"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid token format"));
            }

            String token = authHeader.substring(7);
            String username = userService.getUsernameFromToken(token);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("Invalid token"));
            }

            // Generate new token
            String newToken = userService.generateToken(username);
            Long expiresAt = System.currentTimeMillis() + userService.getTokenExpiration();

            users user = userService.findByUsername(username);
            authResponse response = new authResponse(newToken, expiresAt, userService.toUserDTO(user));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Token refresh failed"));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }
}
