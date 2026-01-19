import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './register.component.html',
    styleUrl: './register.component.css',
})
export class RegisterComponent implements OnInit {
    form!: FormGroup;
    isSubmitting = false;
    isLoading = false; // Skeleton loading during submission
    showSuccess = false;
    errorMessage: string | null = null;
    showPassword = false;
    showConfirmPassword = false;
    private isBrowser: boolean;

    // Feature highlights for left panel
    features = [
        {
            icon: 'discussions',
            title: 'Active Discussions',
            description: 'Join 12K+ conversations on system design and architecture'
        },
        {
            icon: 'experts',
            title: 'Expert Community',
            description: 'Learn from engineers at top tech companies'
        },
        {
            icon: 'resources',
            title: 'Premium Resources',
            description: 'Access curated guides, case studies, and tutorials'
        }
    ];

    constructor(
        private fb: FormBuilder,
        private router: Router,
        private authService: AuthService,
        @Inject(PLATFORM_ID) platformId: Object
    ) {
        this.isBrowser = isPlatformBrowser(platformId);
    }

    ngOnInit(): void {
        this.form = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(8)]],
            confirmPassword: ['', [Validators.required]],
            agreeTerms: [false, [Validators.requiredTrue]],
        }, { validators: this.passwordMatchValidator });
    }

    passwordMatchValidator(form: FormGroup) {
        const password = form.get('password');
        const confirmPassword = form.get('confirmPassword');
        if (password && confirmPassword && password.value !== confirmPassword.value) {
            confirmPassword.setErrors({ passwordMismatch: true });
        }
        return null;
    }

    // Password strength calculation
    get passwordStrength(): { level: string; percent: number; color: string } {
        const password = this.form.get('password')?.value || '';
        let score = 0;

        if (password.length >= 8) score++;
        if (password.length >= 12) score++;
        if (/[A-Z]/.test(password)) score++;
        if (/[0-9]/.test(password)) score++;
        if (/[^A-Za-z0-9]/.test(password)) score++;

        if (score <= 2) {
            return { level: 'Weak', percent: 33, color: '#EF4444' };
        } else if (score <= 3) {
            return { level: 'Medium', percent: 66, color: '#F59E0B' };
        } else {
            return { level: 'Strong', percent: 100, color: '#10B981' };
        }
    }

    togglePasswordVisibility(field: 'password' | 'confirm'): void {
        if (field === 'password') {
            this.showPassword = !this.showPassword;
        } else {
            this.showConfirmPassword = !this.showConfirmPassword;
        }
    }

    onSubmit(): void {
        if (this.form.invalid) {
            return;
        }

        this.isSubmitting = true;
        this.isLoading = true; // Show skeleton during submission
        this.errorMessage = null;

        const { username, email, password } = this.form.value;

        // Call backend registration API
        this.authService.register(username, email, password).subscribe({
            next: (response) => {
                console.log('Registration successful:', response);
                this.showSuccess = true;
                this.isLoading = false;

                // Redirect to dashboard after success (user is auto-logged in)
                setTimeout(() => {
                    this.router.navigate(['/dashboard']);
                }, 2000);
            },
            error: (err) => {
                console.error('Registration error:', err);
                this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
                this.isSubmitting = false;
                this.isLoading = false; // Hide skeleton on error
            }
        });
    }

    // OAuth methods
    registerWithGitHub(): void {
        this.authService.loginWithGitHub();
    }

    registerWithGoogle(): void {
        this.authService.loginWithGoogle();
    }

    // Form getters
    get username() { return this.form.get('username'); }
    get email() { return this.form.get('email'); }
    get password() { return this.form.get('password'); }
    get confirmPassword() { return this.form.get('confirmPassword'); }
    get agreeTerms() { return this.form.get('agreeTerms'); }
}
