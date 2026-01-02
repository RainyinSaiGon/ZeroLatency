import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach, vi } from 'vitest';
import { LoginComponent } from './login.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { PLATFORM_ID } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';

describe('LoginComponent', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;
    let authServiceMock: { login: ReturnType<typeof vi.fn> };

    beforeEach(async () => {
        authServiceMock = {
            login: vi.fn().mockReturnValue(of({ token: 'test-token', user: { id: 1, username: 'test' } }))
        };

        await TestBed.configureTestingModule({
            imports: [LoginComponent, RouterTestingModule, ReactiveFormsModule],
            providers: [
                { provide: PLATFORM_ID, useValue: 'browser' },
                { provide: AuthService, useValue: authServiceMock }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    describe('Component Initialization', () => {
        it('should create the component', () => {
            expect(component).toBeTruthy();
        });

        it('should initialize the form with empty values', () => {
            expect(component.form.get('username')?.value).toBe('');
            expect(component.form.get('password')?.value).toBe('');
            expect(component.form.get('rememberMe')?.value).toBe(false);
        });

        it('should have form invalid when empty', () => {
            expect(component.form.valid).toBe(false);
        });

        it('should initialize isSubmitting as false', () => {
            expect(component.isSubmitting).toBe(false);
        });

        it('should initialize showPassword as false', () => {
            expect(component.showPassword).toBe(false);
        });

        it('should initialize isLoading as false', () => {
            expect(component.isLoading).toBe(false);
        });

        it('should initialize showSuccess as false', () => {
            expect(component.showSuccess).toBe(false);
        });
    });

    describe('Form Validation', () => {
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

        it('should accept valid username', () => {
            const usernameControl = component.form.get('username');
            usernameControl?.setValue('validuser');
            expect(usernameControl?.errors).toBeNull();
        });

        it('should require password', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('');
            expect(passwordControl?.errors?.['required']).toBeTruthy();
        });

        it('should require password to be at least 6 characters', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('12345');
            expect(passwordControl?.errors?.['minlength']).toBeTruthy();
        });

        it('should accept valid password', () => {
            const passwordControl = component.form.get('password');
            passwordControl?.setValue('validpassword');
            expect(passwordControl?.errors).toBeNull();
        });

        it('should have valid form when all fields are correctly filled', () => {
            component.form.setValue({
                username: 'testuser',
                password: 'testpassword',
                rememberMe: false
            });
            expect(component.form.valid).toBe(true);
        });
    });

    describe('Password Visibility Toggle', () => {
        it('should toggle password visibility from false to true', () => {
            component.showPassword = false;
            component.togglePasswordVisibility();
            expect(component.showPassword).toBe(true);
        });

        it('should toggle password visibility from true to false', () => {
            component.showPassword = true;
            component.togglePasswordVisibility();
            expect(component.showPassword).toBe(false);
        });
    });

    describe('Form Submission', () => {
        it('should not submit if form is invalid', () => {
            component.form.setValue({
                username: '',
                password: '',
                rememberMe: false
            });
            component.onSubmit();
            expect(authServiceMock.login).not.toHaveBeenCalled();
        });

        it('should call authService.login on valid form submission', () => {
            component.form.setValue({
                username: 'testuser',
                password: 'testpassword',
                rememberMe: false
            });
            component.onSubmit();
            expect(component.isSubmitting).toBe(true);
            expect(authServiceMock.login).toHaveBeenCalledWith('testuser', 'testpassword');
        });

        it('should set isSubmitting to true during submission', () => {
            component.form.setValue({
                username: 'testuser',
                password: 'testpassword',
                rememberMe: false
            });
            component.onSubmit();
            expect(component.isSubmitting).toBe(true);
        });
    });

    describe('OAuth Methods', () => {
        it('should call loginWithGitHub without errors', () => {
            expect(() => component.loginWithGitHub()).not.toThrow();
        });

        it('should call loginWithGoogle without errors', () => {
            expect(() => component.loginWithGoogle()).not.toThrow();
        });
    });

    describe('Form Getters', () => {
        it('should return username control', () => {
            expect(component.username).toBe(component.form.get('username'));
        });

        it('should return password control', () => {
            expect(component.password).toBe(component.form.get('password'));
        });
    });

    describe('Template Rendering', () => {
        it('should render the brand logo', () => {
            const logo = fixture.nativeElement.querySelector('.brand-name');
            expect(logo.textContent).toContain('Zero Latency');
        });

        it('should render username input field', () => {
            const usernameInput = fixture.nativeElement.querySelector('#username');
            expect(usernameInput).toBeTruthy();
            expect(usernameInput.getAttribute('type')).toBe('text');
        });

        it('should render password input field', () => {
            const passwordInput = fixture.nativeElement.querySelector('#password');
            expect(passwordInput).toBeTruthy();
        });

        it('should render password toggle button', () => {
            const toggleBtn = fixture.nativeElement.querySelector('.password-toggle');
            expect(toggleBtn).toBeTruthy();
        });

        it('should render remember me checkbox', () => {
            const checkbox = fixture.nativeElement.querySelector('.checkbox-wrapper input[type="checkbox"]');
            expect(checkbox).toBeTruthy();
        });

        it('should render sign in button', () => {
            const signInBtn = fixture.nativeElement.querySelector('.btn-primary');
            expect(signInBtn.textContent).toContain('Sign In');
        });

        it('should render OAuth circle buttons', () => {
            const oauthCircles = fixture.nativeElement.querySelectorAll('.oauth-circle');
            expect(oauthCircles.length).toBe(2);
        });

        it('should render create account link', () => {
            const createAccountLink = fixture.nativeElement.querySelector('.signup-link');
            expect(createAccountLink.textContent).toContain('Create an account');
        });

        it('should have link to register page', () => {
            const createAccountLink = fixture.nativeElement.querySelector('.signup-link');
            expect(createAccountLink.getAttribute('routerLink')).toBe('/auth/register');
        });
    });

    describe('Password Input Type Toggle', () => {
        it('should have password input field', () => {
            const passwordInput = fixture.nativeElement.querySelector('#password');
            expect(passwordInput).toBeTruthy();
        });
    });

    describe('Error Display', () => {
        it('should not display error message when errorMessage is null', () => {
            component.errorMessage = null;
            fixture.detectChanges();
            const errorDiv = fixture.nativeElement.querySelector('.error-message');
            expect(errorDiv).toBeNull();
        });
    });
});
