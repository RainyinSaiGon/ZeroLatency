package com.zerolatency.backend.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    @Value("${frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${email.template.base.url:http://localhost:4200/assets/email-templates}")
    private String templateBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Cache for email templates to avoid repeated HTTP calls
    private final Map<String, String> templateCache = new HashMap<>();

    /**
     * Send password reset email using SendGrid
     */
    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/auth/reset-password/" + token;

        String subject = "Reset Your Password - ZeroLatency";
        String htmlContent = getEmailTemplate("password-reset.html", "{{RESET_LINK}}", resetLink);

        try {
            sendEmail(toEmail, subject, htmlContent);
            System.out.println("Password reset email sent successfully to: " + toEmail);
        } catch (IOException e) {
            System.err.println("Failed to send password reset email to: " + toEmail);
            System.err.println("Error: " + e.getMessage());
            // Log the reset link for development purposes
            System.out.println("Password Reset Link: " + resetLink);
        }
    }

    /**
     * Send email verification email using SendGrid
     */
    public void sendVerificationEmail(String toEmail, String token) {
        String verifyLink = frontendUrl + "/auth/verify-email?token=" + token;

        String subject = "Verify Your Email - ZeroLatency";
        String htmlContent = getEmailTemplate("email-verification.html", "{{VERIFY_LINK}}", verifyLink);

        try {
            sendEmail(toEmail, subject, htmlContent);
            System.out.println("Verification email sent successfully to: " + toEmail);
        } catch (IOException e) {
            System.err.println("Failed to send verification email to: " + toEmail);
            System.err.println("Error: " + e.getMessage());
            // Log the verification link for development purposes
            System.out.println("Verification Link: " + verifyLink);
        }
    }

    /**
     * Core method to send email via SendGrid API
     */
    private void sendEmail(String toEmail, String subject, String htmlContent) throws IOException {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        // Log response for debugging
        if (response.getStatusCode() >= 400) {
            throw new IOException("SendGrid API error: " + response.getStatusCode() + " - " + response.getBody());
        }
    }

    /**
     * Fetch email template from frontend and replace placeholders
     */
    private String getEmailTemplate(String templateName, String placeholder, String value) {
        try {
            // Check cache first
            String cacheKey = templateName;
            String template = templateCache.get(cacheKey);

            if (template == null) {
                // Fetch template from frontend
                String templateUrl = templateBaseUrl + "/" + templateName;
                System.out.println("Fetching email template from: " + templateUrl);
                template = restTemplate.getForObject(templateUrl, String.class);

                if (template != null) {
                    // Cache the template
                    templateCache.put(cacheKey, template);
                    System.out.println("Template cached: " + templateName);
                }
            }

            // Replace placeholder with actual value
            if (template != null) {
                return template.replace(placeholder, value);
            } else {
                System.err.println("Failed to fetch template: " + templateName);
                return getFallbackTemplate(placeholder, value);
            }

        } catch (Exception e) {
            System.err.println("Error fetching email template: " + e.getMessage());
            return getFallbackTemplate(placeholder, value);
        }
    }

    /**
     * Fallback template in case frontend templates are unavailable
     */
    private String getFallbackTemplate(String placeholder, String value) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .button { display: inline-block; padding: 12px 24px; background: #1E3A5F; color: white; text-decoration: none; border-radius: 4px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h2>ZeroLatency</h2>
                        <p>Please click the link below:</p>
                        <p><a href="%s" class="button">Click Here</a></p>
                        <p>Or copy this link: %s</p>
                    </div>
                </body>
                </html>
                """
                .formatted(value, value);
    }
}
