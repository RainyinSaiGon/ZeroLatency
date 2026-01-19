# ZeroLatency

A full-stack web application built with Spring Boot and Angular, featuring comprehensive authentication, email services, and modern security practices.

## Technology Stack

### Backend

- Java 21
- Spring Boot 4.0.1
- Spring Security with JWT
- Spring Data JPA
- SendGrid Email Service
- OAuth2 (Google, GitHub)
- Two-Factor Authentication (2FA)
- PostgreSQL/MySQL Database

### Frontend

- Angular 18+
- TypeScript
- RxJS
- Angular Router
- HTTP Client

## Features

- User authentication (local and OAuth2)
- JWT-based session management
- Email verification
- Password reset flow
- Two-factor authentication (2FA)
- Secure password storage with BCrypt
- Email templates with SendGrid integration
- Session timeout and auto-logout
- Protected routes and guards

## Project Structure

```text
ZeroLatency/
├── backend/              # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/zerolatency/backend/
│   │   │   │       ├── config/          # Security, CORS configuration
│   │   │   │       ├── controller/      # REST controllers
│   │   │   │       ├── dto/             # Data Transfer Objects
│   │   │   │       ├── model/           # JPA entities
│   │   │   │       ├── repo/            # JPA repositories
│   │   │   │       ├── security/        # JWT, OAuth2, filters
│   │   │   │       └── service/         # Business logic
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
└── frontend/             # Angular application
    ├── src/
    │   ├── app/
    │   │   ├── auth/                # Authentication module
    │   │   ├── components/          # Shared components
    │   │   ├── dashboard/           # Dashboard module
    │   │   ├── landing/             # Landing page
    │   │   ├── models/              # TypeScript interfaces
    │   │   └── services/            # Angular services
    │   └── assets/
    │       └── email-templates/     # HTML email templates
    └── angular.json
```

## Getting Started

### Prerequisites

- JDK 21 or higher
- Node.js 18+ and npm
- PostgreSQL or MySQL database
- SendGrid account (for email functionality)

### Backend Setup

1. Navigate to the backend directory:

   ```bash
   cd backend
   ```

2. Configure environment variables (create `.env` file):

   ```properties
   # Database
   DB_URL=jdbc:postgresql://localhost:5432/zerolatency
   DB_USERNAME=your_db_username
   DB_PASSWORD=your_db_password

   # JWT
   JWT_SECRET=your-secret-key-min-256-bits
   JWT_EXPIRATION=86400000

   # SendGrid
   SENDGRID_API_KEY=your-sendgrid-api-key
   SENDGRID_FROM_EMAIL=noreply@yourdomain.com
   SENDGRID_FROM_NAME=ZeroLatency Team

   # OAuth2
   GOOGLE_CLIENT_ID=your-google-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   GITHUB_CLIENT_ID=your-github-client-id
   GITHUB_CLIENT_SECRET=your-github-client-secret

   # Frontend URL
   FRONTEND_URL=http://localhost:4200
   EMAIL_TEMPLATE_BASE_URL=http://localhost:4200/assets/email-templates
   ```

3. Build and run:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   Backend will start on `http://localhost:8081`

### Frontend Setup

1. Navigate to the frontend directory:

   ```bash
   cd frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start development server:

   ```bash
   npm start
   ```

   Frontend will start on `http://localhost:4200`

## API Endpoints

### Authentication

- `POST /api/users/register` - Register new user
- `POST /api/users/login` - Login
- `POST /api/users/refresh` - Refresh JWT token
- `GET /api/users/verify-email` - Verify email address
- `POST /api/users/resend-verification` - Resend verification email

### Password Management

- `POST /api/users/forgot-password` - Request password reset
- `POST /api/users/reset-password` - Reset password with token

### Two-Factor Authentication

- `POST /api/users/2fa/setup` - Setup 2FA
- `POST /api/users/2fa/enable` - Enable 2FA
- `POST /api/users/2fa/verify` - Verify 2FA code
- `POST /api/users/2fa/disable` - Disable 2FA

### User Management

- `GET /api/users` - Get user by username
- `GET /api/users/dashboard` - Get all users (admin)

## Security Features

- Password hashing with BCrypt
- JWT-based stateless authentication
- OAuth2 integration (Google, GitHub)
- Email verification
- Two-factor authentication with TOTP
- Backup codes for 2FA recovery
- Session timeout and auto-logout
- CORS configuration
- Protected API endpoints

## Email Templates

Email templates are stored in `frontend/src/assets/email-templates/` as HTML files. The backend fetches these templates via HTTP and injects dynamic content before sending.

Templates:

- `password-reset.html` - Password reset email
- `email-verification.html` - Email verification email

## Development

### Running Tests

Backend:

```bash
cd backend
./mvnw test
```

Frontend:

```bash
cd frontend
npm test
```

### Building for Production

Backend:

```bash
cd backend
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

Frontend:

```bash
cd frontend
npm run build
```

Build artifacts will be in `frontend/dist/`

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is developed for educational purposes.

## Support

For issues and questions, please open an issue on the GitHub repository.
