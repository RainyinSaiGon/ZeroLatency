import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';

@Component({
    selector: 'app-reset-password',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    template: `
    <div class="reset-password-page">
      <div class="bg-pattern"></div>
      
      <div class="reset-password-container">
        <div class="reset-password-box">
          <div *ngIf="!resetSuccess && !invalidToken" class="reset-password-form">
            <div class="reset-password-header">
              <h2>Reset Your Password</h2>
              <p>Enter your new password below.</p>
            </div>

            <div *ngIf="errorMessage" class="error-message">{{ errorMessage }}</div>
            
            <form [formGroup]="form" (ngSubmit)="onSubmit()">
              <div class="form-group">
                <label for="newPassword">New Password</label>
                <input
                  [type]="showPassword ? 'text' : 'password'"
                  id="newPassword"
                  formControlName="newPassword"
                  class="form-control"
                  [class.is-invalid]="newPassword?.invalid && newPassword?.touched"
                  placeholder="Enter new password"
                />
                <button type="button" class="password-toggle" (click)="showPassword = !showPassword">
                  <span *ngIf="!showPassword">👁️</span>
                  <span *ngIf="showPassword">🙈</span>
                </button>
                <span class="error-text" *ngIf="newPassword?.hasError('required') && newPassword?.touched">
                  Password is required
                </span>
                <span class="error-text" *ngIf="newPassword?.hasError('minlength') && newPassword?.touched">
                  Password must be at least 6 characters
                </span>
              </div>

              <div class="form-group">
                <label for="confirmPassword">Confirm Password</label>
                <input
                  [type]="showConfirmPassword ? 'text' : 'password'"
                  id="confirmPassword"
                  formControlName="confirmPassword"
                  class="form-control"
                  [class.is-invalid]="confirmPassword?.invalid && confirmPassword?.touched"
                  placeholder="Confirm new password"
                />
                <button type="button" class="password-toggle" (click)="showConfirmPassword = !showConfirmPassword">
                  <span *ngIf="!showConfirmPassword">👁️</span>
                  <span *ngIf="showConfirmPassword">🙈</span>
                </button>
                <span class="error-text" *ngIf="confirmPassword?.hasError('required') && confirmPassword?.touched">
                  Please confirm your password
                </span>
                <span class="error-text" *ngIf="form.hasError('passwordMismatch') && confirmPassword?.touched">
                  Passwords do not match
                </span>
              </div>

              <button type="submit" class="btn btn-primary" [disabled]="isSubmitting || form.invalid">
                <span *ngIf="!isSubmitting">Reset Password</span>
                <span *ngIf="isSubmitting" class="loading-spinner"></span>
              </button>
            </form>
          </div>

          <div *ngIf="resetSuccess" class="success-message">
            <div class="success-icon">✓</div>
            <h3>Password Reset Successfully</h3>
            <p>Your password has been reset. You can now log in with your new password.</p>
            <a routerLink="/auth/login" class="btn btn-primary">Go to Login</a>
          </div>

          <div *ngIf="invalidToken" class="error-state">
            <div class="error-icon">⚠️</div>
            <h3>Invalid or Expired Link</h3>
            <p>This password reset link is invalid or has expired. Please request a new one.</p>
            <a routerLink="/auth/forgot-password" class="btn btn-primary">Request New Link</a>
          </div>
        </div>
      </div>
    </div>
  `,
    styles: [`
    .reset-password-page {
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

    .reset-password-container {
      position: relative;
      z-index: 1;
      width: 100%;
      max-width: 480px;
    }

    .reset-password-box {
      background: white;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
      border: 1px solid rgba(226, 232, 240, 0.8);
    }

    .reset-password-header {
      margin-bottom: 32px;
      text-align: center;
    }

    .reset-password-header h2 {
      font-size: 28px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 8px;
    }

    .reset-password-header p {
      font-size: 15px;
      color: #64748B;
    }

    .form-group {
      margin-bottom: 20px;
      position: relative;
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
      padding: 12px 48px 12px 14px;
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

    .password-toggle {
      position: absolute;
      right: 12px;
      top: 38px;
      background: none;
      border: none;
      cursor: pointer;
      font-size: 18px;
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
      text-decoration: none;
      color: white;
    }

    .btn-primary {
      background: #1E3A5F;
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

    .success-message, .error-state {
      text-align: center;
    }

    .success-icon, .error-icon {
      width: 64px;
      height: 64px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 32px;
      margin: 0 auto 24px;
    }

    .success-icon {
      background: #10B981;
      color: white;
    }

    .error-icon {
      background: #EF4444;
      color: white;
    }

    .success-message h3, .error-state h3 {
      font-size: 24px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 12px;
    }

    .success-message p, .error-state p {
      font-size: 15px;
      color: #64748B;
      line-height: 1.6;
      margin-bottom: 24px;
    }
  `]
})
export class ResetPasswordComponent implements OnInit {
    form: FormGroup;
    isSubmitting = false;
    resetSuccess = false;
    invalidToken = false;
    errorMessage: string | null = null;
    showPassword = false;
    showConfirmPassword = false;
    private token: string = '';
    private apiUrl = 'http://localhost:8080/api/users';

    constructor(
        private fb: FormBuilder,
        private http: HttpClient,
        private router: Router,
        private route: ActivatedRoute
    ) {
        this.form = this.fb.group({
            newPassword: ['', [Validators.required, Validators.minLength(6)]],
            confirmPassword: ['', [Validators.required]]
        }, { validators: this.passwordMatchValidator });
    }

    ngOnInit(): void {
        this.token = this.route.snapshot.paramMap.get('token') || '';
        if (!this.token) {
            this.invalidToken = true;
        }
    }

    passwordMatchValidator(form: FormGroup) {
        const password = form.get('newPassword');
        const confirmPassword = form.get('confirmPassword');
        if (password && confirmPassword && password.value !== confirmPassword.value) {
            return { passwordMismatch: true };
        }
        return null;
    }

    get newPassword() {
        return this.form.get('newPassword');
    }

    get confirmPassword() {
        return this.form.get('confirmPassword');
    }

    onSubmit(): void {
        if (this.form.invalid) return;

        this.isSubmitting = true;
        this.errorMessage = null;

        this.http.post(`${this.apiUrl}/reset-password`, {
            token: this.token,
            newPassword: this.form.value.newPassword
        }).subscribe({
            next: () => {
                this.resetSuccess = true;
                this.isSubmitting = false;
            },
            error: (err) => {
                this.errorMessage = err.error?.message || 'An error occurred. Please try again.';
                if (this.errorMessage.includes('Invalid') || this.errorMessage.includes('expired')) {
                    this.invalidToken = true;
                }
                this.isSubmitting = false;
            }
        });
    }
}
