import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
    selector: 'app-forgot-password',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    template: `
    <div class="forgot-password-page">
      <div class="bg-pattern"></div>
      
      <div class="forgot-password-container">
        <div class="forgot-password-box">
          <div class="back-link">
            <a routerLink="/auth/login">← Back to Login</a>
          </div>
          
          <div class="forgot-password-header">
            <h2>Forgot Password?</h2>
            <p>Enter your email address and we'll send you a link to reset your password.</p>
          </div>

          <div *ngIf="!emailSent" class="forgot-password-form">
            <div *ngIf="errorMessage" class="error-message">{{ errorMessage }}</div>
            
            <form [formGroup]="form" (ngSubmit)="onSubmit()">
              <div class="form-group">
                <label for="email">Email Address</label>
                <input
                  type="email"
                  id="email"
                  formControlName="email"
                  class="form-control"
                  [class.is-invalid]="email?.invalid && email?.touched"
                  placeholder="your@email.com"
                />
                <span class="error-text" *ngIf="email?.hasError('required') && email?.touched">
                  Email is required
                </span>
                <span class="error-text" *ngIf="email?.hasError('email') && email?.touched">
                  Please enter a valid email
                </span>
              </div>

              <button type="submit" class="btn btn-primary" [disabled]="isSubmitting || form.invalid">
                <span *ngIf="!isSubmitting">Send Reset Link</span>
                <span *ngIf="isSubmitting" class="loading-spinner"></span>
              </button>
            </form>
          </div>

          <div *ngIf="emailSent" class="success-message">
            <div class="success-icon">✓</div>
            <h3>Check Your Email</h3>
            <p>If an account exists with <strong>{{ form.value.email }}</strong>, you will receive a password reset link shortly.</p>
            <a routerLink="/auth/login" class="btn btn-primary">Return to Login</a>
          </div>
        </div>
      </div>
    </div>
  `,
    styles: [`
    .forgot-password-page {
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

    .forgot-password-container {
      position: relative;
      z-index: 1;
      width: 100%;
      max-width: 480px;
    }

    .forgot-password-box {
      background: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
      border: 1px solid rgba(226, 232, 240, 0.8);
    }

    .back-link {
      margin-bottom: 24px;
    }

    .back-link a {
      color: #0F766E;
      text-decoration: none;
      font-size: 14px;
      font-weight: 500;
      transition: color 0.2s;
    }

    .back-link a:hover {
      color: #0D5D56;
    }

    .forgot-password-header {
      margin-bottom: 32px;
    }

    .forgot-password-header h2 {
      font-size: 28px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 8px;
    }

    .forgot-password-header p {
      font-size: 15px;
      color: #64748B;
      line-height: 1.5;
    }

    .form-group {
      margin-bottom: 20px;
    }

    label {
      display: block;
      margin-bottom: 8px;
      color: #334155;
      font-weight: 500;
      font-size: 14px;
    }

    .form-control {
      width: 100%;
      padding: 12px 14px;
      border: 1px solid #E2E8F0;
      border-radius: 8px;
      font-size: 15px;
      transition: all 0.2s ease;
      background: #F8FAFC;
      color: #0F172A;
    }

    .form-control:focus {
      outline: none;
      border-color: #14B8A6;
      background: white;
      box-shadow: 0 0 0 3px rgba(20, 184, 166, 0.1);
    }

    .form-control.is-invalid {
      border-color: #EF4444;
      background: rgba(239, 68, 68, 0.1);
    }

    .error-text {
      color: #DC2626;
      font-size: 13px;
      margin-top: 6px;
      display: block;
    }

    .error-message {
      background: #FEF2F2;
      color: #DC2626;
      padding: 12px 14px;
      border-radius: 8px;
      margin-bottom: 16px;
      border: 1px solid #FEE2E2;
      font-size: 14px;
    }

    .btn {
      width: 100%;
      padding: 14px;
      border: none;
      border-radius: 8px;
      font-size: 15px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s ease;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn-primary {
      background: #1E3A5F;
      color: white;
    }

    .btn-primary:hover:not(:disabled) {
      background: #0F766E;
      transform: translateY(-1px);
    }

    .btn:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }

    .loading-spinner {
      width: 18px;
      height: 18px;
      border: 2px solid rgba(255, 255, 255, 0.3);
      border-top-color: white;
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }

    @keyframes spin {
      to { transform: rotate(360deg); }
    }

    .success-message {
      text-align: center;
    }

    .success-icon {
      width: 64px;
      height: 64px;
      background: #10B981;
      color: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 32px;
      margin: 0 auto 24px;
    }

    .success-message h3 {
      font-size: 24px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 12px;
    }

    .success-message p {
      font-size: 15px;
      color: #64748B;
      line-height: 1.6;
      margin-bottom: 24px;
    }

    .success-message strong {
      color: #0F172A;
    }
  `]
})
export class ForgotPasswordComponent {
    form: FormGroup;
    isSubmitting = false;
    emailSent = false;
    errorMessage: string | null = null;
    private apiUrl = 'http://localhost:8080/api/users';

    constructor(
        private fb: FormBuilder,
        private http: HttpClient
    ) {
        this.form = this.fb.group({
            email: ['', [Validators.required, Validators.email]]
        });
    }

    get email() {
        return this.form.get('email');
    }

    onSubmit(): void {
        if (this.form.invalid) return;

        this.isSubmitting = true;
        this.errorMessage = null;

        this.http.post(`${this.apiUrl}/forgot-password`, { email: this.form.value.email })
            .subscribe({
                next: () => {
                    this.emailSent = true;
                    this.isSubmitting = false;
                },
                error: (err) => {
                    this.errorMessage = err.error?.message || 'An error occurred. Please try again.';
                    this.isSubmitting = false;
                }
            });
    }
}
