import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { RegisterComponent } from './register.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { PLATFORM_ID } from '@angular/core';

describe('RegisterComponent', () => {
    let component: RegisterComponent;
    let fixture: ComponentFixture<RegisterComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [RegisterComponent, RouterTestingModule, ReactiveFormsModule],
            providers: [
                { provide: PLATFORM_ID, useValue: 'browser' }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(RegisterComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    describe('Component Initialization', () => {
        it('should create the component', () => {
            expect(component).toBeTruthy();
        });

        it('should initialize the form with empty values', () => {
            expect(component.form.get('username')?.value).toBe('');
            expect(component.form.get('email')?.value).toBe('');
            expect(component.form.get('password')?.value).toBe('');
            expect(component.form.get('confirmPassword')?.value).toBe('');
            expect(component.form.get('agreeTerms')?.value).toBe(false);
        });

        it('should have form invalid when empty', () => {
            expect(component.form.valid).toBe(false);
        });

        it('should initialize isSubmitting as false', () => {
            expect(component.isSubmitting).toBe(false);
        });

        it('should initialize showSuccess as false', () => {
            expect(component.showSuccess).toBe(false);
        });

        it('should initialize showPassword as false', () => {
            expect(component.showPassword).toBe(false);
        });

        it('should initialize showConfirmPassword as false', () => {
            expect(component.showConfirmPassword).toBe(false);
        });
    });

    describe('Form Validation - Username', () => {
        it('should require username', () => {
            const usernameControl = component.form.get('username');
            usernameControl?.setValue('');
            expect(usernameControl?.errors?.['required']).toBeTruthy();
        });

        it('should require username to be at least 3 characters', () => {
            const usernameControl = component.form.get('username');
            usernameControl?.setValue('ab');
            expect(usernameControl?.errors?.['minlength']).toBeTruthy();
        });

        it('should require username to be at most 20 characters', () => {
            const usernameControl = component.form.get('username');
            usernameControl?.setValue('a'.repeat(21));
            expect(usernameControl?.errors?.['maxlength']).toBeTruthy();
        });

        it('should accept valid username', () => {
            const usernameControl = component.form.get('username');
            usernameControl?.setValue('validuser');
            expect(usernameControl?.errors).toBeNull();
        });
    });

    describe('Form Validation - Email', () => {
        it('should require email', () => {
            const emailControl = component.form.get('email');
            emailControl?.setValue('');
            expect(emailControl?.errors?.['required']).toBeTruthy();
        });

        it('should validate email format', () => {
            const emailControl = component.form.get('email');
            emailControl?.setValue('invalid-email');
            expect(emailControl?.errors?.['email']).toBeTruthy();
        });

        it('should accept valid email', () => {
            const emailControl = component.form.get('email');
            emailControl?.setValue('test@example.com');
            expect(emailControl?.errors).toBeNull();
        });
    });

    describe('Form Validation - Password', () => {
        it('should require password', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('');
            expect(passwordControl?.errors?.['required']).toBeTruthy();
        });

        it('should require password to be at least 8 characters', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('1234567');
            expect(passwordControl?.errors?.['minlength']).toBeTruthy();
        });

        it('should accept valid password', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('validpassword123');
            expect(passwordControl?.errors).toBeNull();
        });
    });

    describe('Form Validation - Confirm Password', () => {
        it('should require confirm password', () => {
            const confirmControl = component.form.get('confirmPassword');
            confirmControl?.setValue('');
            expect(confirmControl?.errors?.['required']).toBeTruthy();
        });

        it('should show error when passwords do not match', () => {
            component.form.get('password')?.setValue('password123');
            component.form.get('confirmPassword')?.setValue('differentpassword');
            component.form.updateValueAndValidity();

            const confirmControl = component.form.get('confirmPassword');
            expect(confirmControl?.errors?.['passwordMismatch']).toBeTruthy();
        });
    });

    describe('Form Validation - Terms Agreement', () => {
        it('should require terms agreement', () => {
            const termsControl = component.form.get('agreeTerms');
            termsControl?.setValue(false);
            expect(termsControl?.errors?.['required']).toBeTruthy();
        });

        it('should accept when terms are agreed', () => {
            const termsControl = component.form.get('agreeTerms');
            termsControl?.setValue(true);
            expect(termsControl?.errors).toBeNull();
        });
    });

    describe('Password Visibility Toggle', () => {
        it('should toggle password visibility for password field', () => {
            component.showPassword = false;
            component.togglePasswordVisibility('password');
            expect(component.showPassword).toBe(true);
        });

        it('should toggle password visibility for confirm field', () => {
            component.showConfirmPassword = false;
            component.togglePasswordVisibility('confirm');
            expect(component.showConfirmPassword).toBe(true);
        });
    });

    describe('Form Submission', () => {
        it('should not submit if form is invalid', () => {
            const consoleSpy = vi.spyOn(console, 'log');
            component.form.setValue({
                username: '',
                email: '',
                password: '',
                confirmPassword: '',
                agreeTerms: false
            });
            component.onSubmit();
            expect(consoleSpy).not.toHaveBeenCalled();
        });

        it('should set isSubmitting to true when form is valid and submitted', () => {
            component.form.setValue({
                username: 'testuser',
                email: 'test@example.com',
                password: 'password123',
                confirmPassword: 'password123',
                agreeTerms: true
            });
            component.onSubmit();
            expect(component.isSubmitting).toBe(true);
        });
    });

    describe('OAuth Methods', () => {
        it('should call registerWithGitHub without errors', () => {
            expect(() => component.registerWithGitHub()).not.toThrow();
        });

        it('should call registerWithGoogle without errors', () => {
            expect(() => component.registerWithGoogle()).not.toThrow();
        });
    });

    describe('Form Getters', () => {
        it('should return username control', () => {
            expect(component.username).toBe(component.form.get('username'));
        });

        it('should return email control', () => {
            expect(component.email).toBe(component.form.get('email'));
        });

        it('should return password control', () => {
            expect(component.password).toBe(component.form.get('password'));
        });

        it('should return confirmPassword control', () => {
            expect(component.confirmPassword).toBe(component.form.get('confirmPassword'));
        });

        it('should return agreeTerms control', () => {
            expect(component.agreeTerms).toBe(component.form.get('agreeTerms'));
        });
    });

    describe('Template Rendering', () => {
        it('should render the brand logo', () => {
            const logo = fixture.nativeElement.querySelector('.brand-name');
            expect(logo.textContent).toContain('Zero Latency');
        });

        it('should render hero section with title', () => {
            const heroTitle = fixture.nativeElement.querySelector('.hero-content h1');
            expect(heroTitle.textContent).toContain('Join the community');
        });

        it('should render benefits list', () => {
            const benefits = fixture.nativeElement.querySelectorAll('.benefit-item');
            expect(benefits.length).toBe(3);
        });

        it('should render username input field', () => {
            const usernameInput = fixture.nativeElement.querySelector('#username');
            expect(usernameInput).toBeTruthy();
        });

        it('should render email input field', () => {
            const emailInput = fixture.nativeElement.querySelector('#email');
            expect(emailInput).toBeTruthy();
            expect(emailInput.getAttribute('type')).toBe('email');
        });

        it('should render password input field', () => {
            const passwordInput = fixture.nativeElement.querySelector('#password');
            expect(passwordInput).toBeTruthy();
        });

        it('should render confirm password input field', () => {
            const confirmInput = fixture.nativeElement.querySelector('#confirmPassword');
            expect(confirmInput).toBeTruthy();
        });

        it('should render password toggle buttons', () => {
            const toggleBtns = fixture.nativeElement.querySelectorAll('.password-toggle');
            expect(toggleBtns.length).toBe(2);
        });

        it('should render terms checkbox', () => {
            const checkbox = fixture.nativeElement.querySelector('.terms-checkbox input[type="checkbox"]');
            expect(checkbox).toBeTruthy();
        });

        it('should render create account button', () => {
            const createBtn = fixture.nativeElement.querySelector('.btn-primary');
            expect(createBtn.textContent).toContain('Create Account');
        });

        it('should render OAuth circle buttons', () => {
            const oauthCircles = fixture.nativeElement.querySelectorAll('.oauth-circle');
            expect(oauthCircles.length).toBe(2);
        });

        it('should render sign in link', () => {
            const signInLink = fixture.nativeElement.querySelector('.login-link');
            expect(signInLink.textContent).toContain('Sign in');
        });

        it('should have link to login page', () => {
            const signInLink = fixture.nativeElement.querySelector('.login-link');
            expect(signInLink.getAttribute('routerLink')).toBe('/auth/login');
        });
    });

    describe('Valid Form State', () => {
        it('should have valid form when all fields are correctly filled', () => {
            component.form.setValue({
                username: 'testuser',
                email: 'test@example.com',
                password: 'password123',
                confirmPassword: 'password123',
                agreeTerms: true
            });
            expect(component.form.valid).toBe(true);
        });
    });

    describe('Success Animation', () => {
        it('should have showSuccess property', () => {
            expect(typeof component.showSuccess).toBe('boolean');
        });
    });
});
