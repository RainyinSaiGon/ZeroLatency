package com.zerolatency.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${email.service:none}")
    private String emailService;

    @Value("${email.from:noreply@zerolatency.com}")
    private String emailFrom;

    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    /**
     * Send password reset email
     * TODO: Implement actual email sending based on configured service (SendGrid,
     * AWS SES, SMTP)
     */
    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/auth/reset-password/" + token;

        // For now, just log the reset link
        // In production, this should send an actual email
        System.out.println("=".repeat(80));
        System.out.println("PASSWORD RESET EMAIL");
        System.out.println("To: " + toEmail);
        System.out.println("Reset Link: " + resetLink);
        System.out.println("This link will expire in 1 hour.");
        System.out.println("=".repeat(80));

        // TODO: Implement email sending
        // Example for SendGrid:
        // sendGridService.send(toEmail, "Password Reset", resetLink);
    }

    /**
     * Send email verification email
     */
    public void sendVerificationEmail(String toEmail, String token) {
        String verifyLink = frontendUrl + "/auth/verify-email?token=" + token;

        System.out.println("=".repeat(80));
        System.out.println("EMAIL VERIFICATION");
        System.out.println("To: " + toEmail);
        System.out.println("Verification Link: " + verifyLink);
        System.out.println("=".repeat(80));

        // TODO: Implement email sending
    }
}
