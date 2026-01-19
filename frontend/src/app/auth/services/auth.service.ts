import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, tap } from "rxjs";
import { AuthResponse, LoginRequest, RegisterRequest } from "../auth.model";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";

@Injectable({
    providedIn: "root"
})
export class AuthService {
    private apiURL = 'http://localhost:8080/api/users';

    private currentUserSubject = new BehaviorSubject<any>(this.getUserFromStorage());
    public currentUser$ = this.currentUserSubject.asObservable();

    private isAuthenticatedSubject = new BehaviorSubject<boolean>(!!this.getToken());
    public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();


    constructor(private http: HttpClient, private router: Router) { }


    login(username: string, password: string): Observable<AuthResponse> {
        const credentials: LoginRequest = { username, password };
        return this.http.post<AuthResponse>(`${this.apiURL}/login`, credentials).pipe(
            tap((response) => {
                this.setCurrentUser(response.user);
                this.setToken(response.token, response.expiresAt);
                this.currentUserSubject.next(response.user);
                this.isAuthenticatedSubject.next(true);
            }
            )
        );
    }

    register(username: string, email: string, password: string): Observable<AuthResponse> {
        const request: RegisterRequest = { username, email, password };
        return this.http.post<AuthResponse>(`${this.apiURL}/register`, request).pipe(
            tap((response) => {
                this.setCurrentUser(response.user);
                this.setToken(response.token, response.expiresAt);
                this.currentUserSubject.next(response.user);
                this.isAuthenticatedSubject.next(true);
            }
            )
        );
    }

    loginWithGoogle(): void {
        // Redirect to backend OAuth2 endpoint
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    }

    loginWithGitHub(): void {
        // Redirect to backend OAuth2 endpoint
        window.location.href = 'http://localhost:8080/oauth2/authorization/github';
    }

    handleOAuthCallback(token: string, expiresAt: number): void {
        // Store token and expiration
        this.setToken(token, expiresAt);

        // Fetch user info (you might want to decode JWT or call /me endpoint)
        // For now, just mark as authenticated
        this.isAuthenticatedSubject.next(true);

        // Redirect to dashboard
        this.router.navigate(['/dashboard']);
    }

    refreshToken(): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiURL}/refresh`, {}).pipe(
            tap((response) => {
                this.setToken(response.token, response.expiresAt);
                this.setCurrentUser(response.user);
                this.currentUserSubject.next(response.user);
                this.isAuthenticatedSubject.next(true);
            })
        );
    }

    logout(): void {
        this.removeToken();
        this.removeCurrentUser();
        this.currentUserSubject.next(null);
        this.isAuthenticatedSubject.next(false);

    }

    isAuthenticated(): boolean {
        const token = this.getToken();
        if (!token) return false;

        // Check if token is expired
        const expiresAt = this.getTokenExpiration();
        if (expiresAt && Date.now() > expiresAt) {
            this.logout();
            return false;
        }

        return true;
    }

    getToken(): string | null {
        if (typeof window === 'undefined') return null;
        return localStorage.getItem('authToken')
    }

    getTokenExpiration(): number | null {
        if (typeof window === 'undefined') return null;
        const expiresAt = localStorage.getItem('tokenExpiration');
        return expiresAt ? parseInt(expiresAt) : null;
    }

    setToken(token: string, expiresAt?: number): void {
        if (typeof window === 'undefined') return;
        localStorage.setItem('authToken', token);
        if (expiresAt) {
            localStorage.setItem('tokenExpiration', expiresAt.toString());
        }
    }

    private removeToken(): void {
        if (typeof window === 'undefined') return;
        localStorage.removeItem('authToken');
        localStorage.removeItem('tokenExpiration');
    }

    private setCurrentUser(user: any): void {
        if (typeof window === 'undefined') return;
        localStorage.setItem('currentUser', JSON.stringify(user));
    }

    private removeCurrentUser(): void {
        if (typeof window === 'undefined') return;
        localStorage.removeItem('currentUser');
    }

    getCurrentUser(): any {
        return this.currentUserSubject.value;
    }

    private getUserFromStorage(): any {
        if (typeof window === 'undefined') return null;
        const user = localStorage.getItem('currentUser');
        return user ? JSON.parse(user) : null;
    }
}
