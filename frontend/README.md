# ZeroLatency Frontend

Angular-based frontend application providing user authentication, dashboard, and account management features.

## Technology Stack

- Angular 18+
- TypeScript
- RxJS
- Angular Router
- HTTP Client
- Angular Forms

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── auth/                        # Authentication module
│   │   │   ├── components/
│   │   │   │   ├── login/               # Login component
│   │   │   │   ├── register/            # Registration component
│   │   │   │   ├── forgot-password/     # Forgot password
│   │   │   │   ├── reset-password/      # Reset password
│   │   │   │   ├── verify-email/        # Email verification
│   │   │   │   └── oauth2-callback/     # OAuth2 callback handler
│   │   │   └── services/
│   │   │       └── auth.service.ts      # Authentication service
│   │   ├── components/
│   │   │   └── session-timeout-modal/   # Session timeout modal
│   │   ├── dashboard/                   # Dashboard module
│   │   │   └── dashboard.component.ts
│   │   ├── landing/                     # Landing page
│   │   │   └── landing.component.ts
│   │   ├── models/                      # TypeScript interfaces
│   │   │   └── user.model.ts
│   │   ├── services/                    # Shared services
│   │   │   ├── session.service.ts       # Session management
│   │   │   └── users.service.ts         # User API service
│   │   ├── app.config.ts                # App configuration
│   │   ├── app.routes.ts                # Route definitions
│   │   └── app.ts                       # Root component
│   ├── assets/
│   │   └── email-templates/             # Email HTML templates
│   │       ├── password-reset.html
│   │       └── email-verification.html
│   └── styles.css                       # Global styles
├── angular.json                         # Angular CLI configuration
├── package.json                         # Dependencies
└── tsconfig.json                        # TypeScript configuration
```

## Setup

### Prerequisites

- Node.js 18+ and npm
- Angular CLI (optional but recommended)

### Installation

1. Install dependencies:

   ```bash
   npm install
   ```

2. Install Angular CLI globally (optional):

   ```bash
   npm install -g @angular/cli
   ```

### Development Server

Start the development server:

```bash
npm start
```

Or using Angular CLI:

```bash
ng serve
```

Navigate to `http://localhost:4200/`. The application will automatically reload when you change source files.

## Features

### Authentication

- Local authentication (username/password)
- OAuth2 authentication (Google, GitHub)
- Email verification
- Password reset flow
- Two-factor authentication (2FA)
- Session management with auto-logout

### User Interface

- Landing page
- Login/Register forms
- Dashboard
- Profile management
- Session timeout modal
- Responsive design

### Security

- JWT token storage
- Automatic token refresh
- Protected routes with guards
- Session timeout detection
- Secure password requirements

## Routing

| Route | Component | Description | Protected |
|-------|-----------|-------------|-----------|
| `/` | Landing | Landing page | No |
| `/auth/login` | Login | User login | No |
| `/auth/register` | Register | User registration | No |
| `/auth/forgot-password` | ForgotPassword | Request password reset | No |
| `/auth/reset-password/:token` | ResetPassword | Reset password | No |
| `/auth/verify-email` | VerifyEmail | Email verification | No |
| `/auth/oauth2/callback` | OAuth2Callback | OAuth2 redirect handler | No |
| `/dashboard` | Dashboard | User dashboard | Yes |

## Services

### AuthService

Handles authentication operations:

- `login(username, password)` - User login
- `register(username, email, password)` - User registration
- `logout()` - User logout
- `refreshToken()` - Refresh JWT token
- `forgotPassword(email)` - Request password reset
- `resetPassword(token, newPassword)` - Reset password
- `verifyEmail(token)` - Verify email address
- `setup2FA()` - Setup two-factor authentication
- `enable2FA(code)` - Enable 2FA
- `verify2FA(username, code)` - Verify 2FA code
- `disable2FA(password)` - Disable 2FA

### SessionService

Manages user sessions:

- Tracks user activity
- Handles session timeout
- Automatic logout on inactivity
- Token refresh management

### UsersService

User management operations:

- `getUserByUsername(username)` - Get user details
- `getAllUsers()` - Get all users (admin)

## Building

### Development Build

```bash
npm run build
```

### Production Build

```bash
npm run build -- --configuration production
```

Build artifacts will be stored in the `dist/` directory.

## Testing

### Unit Tests

Run unit tests with Vitest:

```bash
npm test
```

### End-to-End Tests

```bash
npm run e2e
```

Note: E2E testing framework needs to be configured separately.

## Code Scaffolding

Generate a new component:

```bash
ng generate component component-name
```

Generate a new service:

```bash
ng generate service service-name
```

For more options:

```bash
ng generate --help
```

## Configuration

### Environment Variables

The application uses Angular's environment configuration. Update `src/environments/` files for different environments.

### API Endpoint

The backend API endpoint is configured in the services. Default: `http://localhost:8081/api`

## Email Templates

Email templates are served as static assets from `src/assets/email-templates/`. The backend fetches these templates to send emails.

Templates use placeholders for dynamic content:

- `{{RESET_LINK}}` - Password reset link
- `{{VERIFY_LINK}}` - Email verification link

## Styling

The application uses vanilla CSS for styling. Global styles are in `src/styles.css`, and component-specific styles are in their respective component files.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development Guidelines

### Code Style

- Follow Angular style guide
- Use TypeScript strict mode
- Implement proper error handling
- Add comments for complex logic

### Component Structure

- Keep components focused and single-purpose
- Use services for business logic
- Implement OnDestroy for cleanup
- Use reactive forms for complex forms

### State Management

- Use RxJS for reactive state
- Implement proper subscription management
- Use BehaviorSubject for shared state

## Troubleshooting

### CORS Issues

Ensure backend CORS configuration allows requests from `http://localhost:4200`

### Authentication Errors

- Check JWT token in localStorage
- Verify token expiration
- Check network requests in browser DevTools

### Build Errors

- Clear node_modules and reinstall: `rm -rf node_modules && npm install`
- Clear Angular cache: `ng cache clean`

## Additional Resources

- [Angular Documentation](https://angular.dev)
- [Angular CLI](https://angular.dev/tools/cli)
- [RxJS Documentation](https://rxjs.dev/)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
