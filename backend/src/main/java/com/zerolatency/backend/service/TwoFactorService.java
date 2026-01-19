package com.zerolatency.backend.service;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class TwoFactorService {

    private final DefaultSecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    /**
     * Generate a new TOTP secret for a user
     */
    public String generateSecret() {
        return secretGenerator.generate();
    }

    /**
     * Generate QR code data URL for authenticator apps
     */
    public String generateQRCode(String secret, String username) throws QrGenerationException {
        QrData data = new QrData.Builder()
                .label(username)
                .secret(secret)
                .issuer("Zero Latency")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = generator.generate(data);

        // Convert to base64 data URL
        String base64Image = Base64.getEncoder().encodeToString(imageData);
        return "data:image/png;base64," + base64Image;
    }

    /**
     * Verify a TOTP code
     */
    public boolean verifyCode(String secret, String code) {
        return verifier.isValidCode(secret, code);
    }

    /**
     * Generate backup codes (10 codes)
     */
    public List<String> generateBackupCodes() {
        List<String> codes = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 10; i++) {
            // Generate 8-character alphanumeric code
            String code = String.format("%08d", random.nextInt(100000000));
            codes.add(code);
        }

        return codes;
    }
}
