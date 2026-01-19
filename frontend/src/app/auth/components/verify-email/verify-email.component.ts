import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
    selector: 'app-verify-email',
    standalone: true,
    imports: [CommonModule, RouterLink],
    template: `
    <div class="verify-email-page">
      <div class="bg-pattern"></div>
      
      <div class="verify-email-container">
        <div class="verify-email-box">
          <div *ngIf="isVerifying" class="verifying-state">
            <div class="spinner"></div>
            <h2>Verifying Your Email...</h2>
            <p>Please wait while we verify your email address.</p>
          </div>

          <div *ngIf="verificationSuccess" class="success-state">
            <div class="success-icon">✓</div>
            <h2>Email Verified!</h2>
            <p>Your email has been successfully verified. You can now access all features.</p>
            <a routerLink="/auth/login" class="btn btn-primary">Go to Login</a>
          </div>

          <div *ngIf="verificationError" class="error-state">
            <div class="error-icon">⚠️</div>
            <h2>Verification Failed</h2>
            <p>{{ errorMessage }}</p>
            <a routerLink="/auth/login" class="btn btn-secondary">Back to Login</a>
          </div>
        </div>
      </div>
    </div>
  `,
    styles: [`
    .verify-email-page {
      min-height: 100vh;
      background: linear-gradient(135deg, #F8FAFC 0%, #E2E8F0 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      padding: 20px;
    }

    .bg-pattern {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background-image:
        radial-gradient(circle at 25% 25%, rgba(15, 118, 110, 0.08) 0%, transparent 50%),
        radial-gradient(circle at 75% 75%, rgba(59, 130, 246, 0.06) 0%, transparent 50%);
      pointer-events: none;
    }

    .verify-email-container {
      position: relative;
      z-index: 1;
      width: 100%;
      max-width: 480px;
    }

    .verify-email-box {
      background: white;
      padding: 60px 40px;
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
      border: 1px solid rgba(226, 232, 240, 0.8);
      text-align: center;
    }

    .verifying-state, .success-state, .error-state {
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .spinner {
      width: 64px;
      height: 64px;
      border: 4px solid #E2E8F0;
      border-top-color: #0F766E;
      border-radius: 50%;
      animation: spin 1s linear infinite;
      margin-bottom: 24px;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    .success-icon, .error-icon {
      width: 80px;
      height: 80px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 40px;
      margin-bottom: 24px;
    }

    .success-icon {
      background: #10B981;
      color: white;
    }

    .error-icon {
      background: #EF4444;
      color: white;
    }

    h2 {
      font-size: 28px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 12px;
    }

    p {
      font-size: 15px;
      color: #64748B;
      line-height: 1.6;
      margin-bottom: 24px;
    }

    .btn {
      padding: 14px 32px;
      border: none;
      border-radius: 8px;
      font-size: 15px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s ease;
      text-decoration: none;
      display: inline-block;
    }

    .btn-primary {
      background: #1E3A5F;
      color: white;
    }

    .btn-primary:hover {
      background: #0F766E;
      transform: translateY(-1px);
    }

    .btn-secondary {
      background: #E2E8F0;
      color: #0F172A;
    }

    .btn-secondary:hover {
      background: #CBD5E1;
    }
  `]
})
export class VerifyEmailComponent implements OnInit {
    isVerifying = true;
    verificationSuccess = false;
    verificationError = false;
    errorMessage = '';
    private apiUrl = 'http://localhost:8080/api/users';

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private http: HttpClient
    ) { }

    ngOnInit(): void {
        const token = this.route.snapshot.queryParamMap.get('token');

        if (!token) {
            this.isVerifying = false;
            this.verificationError = true;
            this.errorMessage = 'Invalid verification link. Please check your email for the correct link.';
            return;
        }

        this.verifyEmail(token);
    }

    verifyEmail(token: string): void {
        this.http.get(`${this.apiUrl}/verify-email?token=${token}`)
            .subscribe({
                next: () => {
                    this.isVerifying = false;
                    this.verificationSuccess = true;
                },
                error: (err) => {
                    this.isVerifying = false;
                    this.verificationError = true;
                    this.errorMessage = err.error?.message || 'Verification failed. The link may have expired.';
                }
            });
    }
}
