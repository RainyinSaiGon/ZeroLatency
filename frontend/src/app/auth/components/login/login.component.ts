import { Component, OnInit, OnDestroy, Inject, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  form!: FormGroup;
  isSubmitting = false;
  isLoading = false; // Disabled skeleton - was causing issues
  showSuccess = false;
  errorMessage: string | null = null;
  returnUrl: string = '/';
  showPassword = false;
  private isBrowser: boolean;

  // Testimonials for carousel
  testimonials = [
    {
      quote: "Zero Latency transformed how our team discusses architecture decisions. The quality of insights here is unmatched.",
      author: "Sarah Chen",
      role: "Principal Engineer at Stripe",
      avatar: "SC"
    },
    {
      quote: "Finally, a community that understands the nuances of distributed systems. I've learned more here than in years of conferences.",
      author: "Marcus Rodriguez",
      role: "Staff Architect at Netflix",
      avatar: "MR"
    },
    {
      quote: "The real-world case studies and post-mortems shared here are invaluable. This is where senior engineers belong.",
      author: "Emily Zhang",
      role: "Tech Lead at Coinbase",
      avatar: "EZ"
    }
  ];
  currentTestimonialIndex = 0;
  private testimonialInterval: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    // Create form with validation rules
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      rememberMe: [false],
    });

    // Get redirect URL from route params 
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';

    // Check for OAuth callback
    const token = this.route.snapshot.queryParams['token'];
    const expiresAt = this.route.snapshot.queryParams['expiresAt'];

    if (token && expiresAt) {
      // Handle OAuth callback
      this.authService.handleOAuthCallback(token, parseInt(expiresAt));
      return; // Skip the rest of initialization
    }

    if (this.isBrowser) {
      // Start testimonial rotation
      this.testimonialInterval = setInterval(() => {
        this.nextTestimonial();
      }, 5000);
    }
  }

  ngOnDestroy(): void {
    if (this.testimonialInterval) {
      clearInterval(this.testimonialInterval);
    }
  }

  get currentTestimonial() {
    return this.testimonials[this.currentTestimonialIndex];
  }

  nextTestimonial(): void {
    this.currentTestimonialIndex = (this.currentTestimonialIndex + 1) % this.testimonials.length;
  }

  goToTestimonial(index: number): void {
    if (index !== this.currentTestimonialIndex) {
      this.currentTestimonialIndex = index;
    }
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      // Trigger shake animation on invalid fields
      return;
    }

    this.isSubmitting = true;
    this.isLoading = true; // Show skeleton during submission
    this.errorMessage = null;

    const { username, password, rememberMe } = this.form.value;

    // Call auth service to login
    this.authService.login(username, password).subscribe({
      next: (response) => {
        console.log('Login successful:', response);

        // Show success animation
        this.showSuccess = true;
        this.isLoading = false;

        // Store remember me preference
        if (rememberMe) {
          localStorage.setItem('rememberMe', 'true');
        }

        // Navigate after animation
        setTimeout(() => {
          this.router.navigateByUrl(this.returnUrl);
        }, 1500);
      },
      error: (err) => {
        console.error('Login error:', err);
        this.errorMessage = err.error?.message || 'Invalid username or password';
        this.isSubmitting = false;
        this.isLoading = false; // Hide skeleton on error
      },
    });
  }

  // OAuth login methods
  loginWithGitHub(): void {
    this.authService.loginWithGitHub();
  }

  loginWithGoogle(): void {
    this.authService.loginWithGoogle();
  }

  // Helper getters for template
  get username() {
    return this.form.get('username');
  }

  get password() {
    return this.form.get('password');
  }
}