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
import com.zerolatency.backend.service.TwoFactorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class usersController {
    @Autowired
    private usersService userService;

    @Autowired
    private TwoFactorService twoFactorService;

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Email is required"));
            }

            userService.requestPasswordReset(email);

            // Always return success to prevent email enumeration
            Map<String, String> response = new HashMap<>();
            response.put("message", "If an account exists with this email, a password reset link has been sent.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");

            if (token == null || newPassword == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Token and new password are required"));
            }

            if (newPassword.length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Password must be at least 6 characters"));
            }

            userService.resetPassword(token, newPassword);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password has been reset successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred"));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            userService.verifyEmail(token);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Email verified successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred"));
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Email is required"));
            }

            userService.resendVerificationEmail(email);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Verification email sent");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("An error occurred"));
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

    // 2FA Endpoints
    @PostMapping("/2fa/setup")
    public ResponseEntity<?> setup2FA(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = userService.getUsernameFromToken(token);
            users user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            // Generate secret and QR code
            String secret = twoFactorService.generateSecret();
            String qrCode = twoFactorService.generateQRCode(secret, user.getUsername());
            List<String> backupCodes = twoFactorService.generateBackupCodes();

            // Store secret temporarily (not enabled yet)
            user.setTwoFactorSecret(secret);
            user.setBackupCodes(String.join(",", backupCodes));
            userService.saveUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("secret", secret);
            response.put("qrCode", qrCode);
            response.put("backupCodes", backupCodes);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to setup 2FA: " + e.getMessage()));
        }
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<?> enable2FA(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {
        try {
            String token = authHeader.substring(7);
            String username = userService.getUsernameFromToken(token);
            users user = userService.findByUsername(username);

            if (user == null || user.getTwoFactorSecret() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("2FA setup not completed"));
            }

            String code = request.get("code");
            if (!twoFactorService.verifyCode(user.getTwoFactorSecret(), code)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Invalid verification code"));
            }

            // Enable 2FA
            user.setTwoFactorEnabled(true);
            userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "2FA enabled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to enable 2FA"));
        }
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<?> verify2FA(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String code = request.get("code");

            users user = userService.findByUsername(username);
            if (user == null || !user.getTwoFactorEnabled()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("2FA not enabled for this user"));
            }

            // Check if it's a backup code
            String[] backupCodes = user.getBackupCodes().split(",");
            boolean isBackupCode = false;
            for (String backupCode : backupCodes) {
                if (backupCode.equals(code)) {
                    isBackupCode = true;
                    // Remove used backup code
                    String updatedCodes = user.getBackupCodes().replace(code + ",", "").replace("," + code, "");
                    user.setBackupCodes(updatedCodes);
                    userService.saveUser(user);
                    break;
                }
            }

            if (!isBackupCode && !twoFactorService.verifyCode(user.getTwoFactorSecret(), code)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Invalid verification code"));
            }

            // Generate JWT token
            String jwtToken = userService.generateToken(username);
            Long expiresAt = System.currentTimeMillis() + userService.getTokenExpiration();

            authResponse response = new authResponse(jwtToken, expiresAt, userService.toUserDTO(user));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("2FA verification failed"));
        }
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disable2FA(@RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> request) {
        try {
            String token = authHeader.substring(7);
            String username = userService.getUsernameFromToken(token);
            users user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(createErrorResponse("User not found"));
            }

            String password = request.get("password");
            if (!userService.verifyPassword(username, password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Invalid password"));
            }

            // Disable 2FA
            user.setTwoFactorEnabled(false);
            user.setTwoFactorSecret(null);
            user.setBackupCodes(null);
            userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "2FA disabled successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to disable 2FA"));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("message", message);
        return error;
    }
}
