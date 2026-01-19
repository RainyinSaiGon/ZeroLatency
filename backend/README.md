# ZeroLatency Backend

REST API backend service built with Spring Boot, providing authentication, user management, and email services.

## Technology Stack

- Java 21
- Spring Boot 4.0.1
- Spring Security
- Spring Data JPA
- JWT Authentication
- OAuth2 (Google, GitHub)
- SendGrid Email Service
- BCrypt Password Encryption
- PostgreSQL/MySQL

## Project Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/zerolatency/backend/
│   │   │   ├── config/                  # Application configuration
│   │   │   │   ├── CorsConfig.java      # CORS settings
│   │   │   │   └── SecurityConfig.java  # Security configuration
│   │   │   ├── controller/              # REST controllers
│   │   │   │   └── UserController.java  # User endpoints
│   │   │   ├── dto/                     # Data Transfer Objects
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── UsernameRequest.java
│   │   │   │   └── UserDTO.java
│   │   │   ├── model/                   # JPA entities
│   │   │   │   ├── User.java
│   │   │   │   ├── AuthProvider.java
│   │   │   │   ├── EmailVerificationToken.java
│   │   │   │   └── PasswordResetToken.java
│   │   │   ├── repo/                    # JPA repositories
│   │   │   │   ├── UserRepo.java
│   │   │   │   ├── EmailVerificationTokenRepo.java
│   │   │   │   └── PasswordResetTokenRepo.java
│   │   │   ├── security/                # Security components
│   │   │   │   ├── JwtUtil.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── CustomOAuth2UserService.java
│   │   │   │   └── OAuth2SuccessHandler.java
│   │   │   └── service/                 # Business logic
│   │   │       ├── UserService.java
│   │   │       ├── EmailService.java
│   │   │       └── TwoFactorService.java
│   │   └── resources/
│   │       └── application.properties   # Configuration
│   └── test/                            # Unit tests
└── pom.xml                              # Maven dependencies
```

## Setup

### Prerequisites

- JDK 21 or higher
- Maven 3.6+
- PostgreSQL or MySQL database
- SendGrid account

### Configuration

Create a `.env` file or set environment variables:

```properties
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/zerolatency
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your-secret-key-minimum-256-bits
JWT_EXPIRATION=86400000

# SendGrid Email Service
SENDGRID_API_KEY=your-sendgrid-api-key
SENDGRID_FROM_EMAIL=noreply@yourdomain.com
SENDGRID_FROM_NAME=ZeroLatency Team

# OAuth2 - Google
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# OAuth2 - GitHub
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# Frontend URL
FRONTEND_URL=http://localhost:4200
EMAIL_TEMPLATE_BASE_URL=http://localhost:4200/assets/email-templates
```

Update `src/main/resources/application.properties` to reference these variables.

### Running the Application

1. Build the project:

   ```bash
   ./mvnw clean install
   ```

2. Run the application:

   ```bash
   ./mvnw spring-boot:run
   ```

3. The API will be available at `http://localhost:8081`

## API Endpoints

### Authentication Endpoints

| Method | Endpoint                 | Description          |
|--------|--------------------------|----------------------|
| POST   | `/api/users/register`    | Register new user    |
| POST   | `/api/users/login`       | User login           |
| POST   | `/api/users/refresh`     | Refresh JWT token    |

### Email Verification

| Method | Endpoint                          | Description                |
|--------|-----------------------------------|----------------------------|
| GET    | `/api/users/verify-email`         | Verify email with token    |
| POST   | `/api/users/resend-verification`  | Resend verification email  |

### Password Management

| Method | Endpoint                       | Description                 |
|--------|--------------------------------|-----------------------------|
| POST   | `/api/users/forgot-password`   | Request password reset      |
| POST   | `/api/users/reset-password`    | Reset password with token   |

### Two-Factor Authentication

| Method | Endpoint                 | Description              |
|--------|--------------------------|--------------------------|
| POST   | `/api/users/2fa/setup`   | Setup 2FA (get QR code)  |
| POST   | `/api/users/2fa/enable`  | Enable 2FA               |
| POST   | `/api/users/2fa/verify`  | Verify 2FA code          |
| POST   | `/api/users/2fa/disable` | Disable 2FA              |

### User Management

| Method | Endpoint                | Description           |
|--------|-------------------------|-----------------------|
| GET    | `/api/users`            | Get user by username  |
| GET    | `/api/users/dashboard`  | Get all users         |

## Security Features

### Authentication Flow

1. User registers or logs in
2. Backend validates credentials
3. JWT token generated and returned
4. Client includes token in Authorization header for subsequent requests
5. Token validated on each request via JwtAuthenticationFilter

### Password Security

- Passwords hashed using BCrypt with strength 12
- Password reset tokens expire after 1 hour
- Email verification required for new accounts

### 2FA Features

- TOTP-based (Time-based One-Time Password)
- QR code generation for authenticator apps
- Backup codes for account recovery
- Secure secret storage

### OAuth2 Integration

- Google OAuth2
- GitHub OAuth2
- Automatic user creation for OAuth2 users
- Provider-specific attribute handling

## Email Service

The email service uses SendGrid for sending transactional emails. Email templates are fetched from the frontend as HTML files.

### Template System

- Templates stored in frontend: `frontend/src/assets/email-templates/`
- Backend fetches templates via HTTP
- Template caching for performance
- Fallback templates for error cases
- Dynamic content injection via placeholders

### Available Templates

- `password-reset.html` - Password reset email
- `email-verification.html` - Email verification email

## Database Schema

### Users Table

- `user_id` (Primary Key)
- `username`
- `email` (Unique)
- `password` (BCrypt hashed)
- `role` (USER, ADMIN)
- `provider` (LOCAL, GOOGLE, GITHUB)
- `provider_id`
- `profile_picture`
- `enabled`
- `email_verified`
- `two_factor_enabled`
- `two_factor_secret`
- `backup_codes`
- `created_at`

### Password Reset Tokens

- `id` (Primary Key)
- `token` (Unique)
- `user_id` (Foreign Key)
- `expiry_date`

### Email Verification Tokens

- `id` (Primary Key)
- `token` (Unique)
- `user_id` (Foreign Key)
- `expiry_date`

## Testing

Run unit tests:

```bash
./mvnw test
```

Run with coverage:

```bash
./mvnw test jacoco:report
```

## Building for Production

Create JAR file:

```bash
./mvnw clean package
```

Run JAR:

```bash
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## Environment Variables

All sensitive configuration should be externalized via environment variables. Never commit secrets to version control.

## Troubleshooting

### Database Connection Issues

- Verify database is running
- Check connection URL, username, and password
- Ensure database exists

### Email Not Sending

- Verify SendGrid API key is valid
- Check sender email is verified in SendGrid
- Review SendGrid dashboard for errors

### JWT Token Issues

- Ensure JWT_SECRET is at least 256 bits
- Check token expiration settings
- Verify Authorization header format: `Bearer <token>`

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [SendGrid Java Library](https://github.com/sendgrid/sendgrid-java)
- [JWT.io](https://jwt.io/)
