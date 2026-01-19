import { Injectable, NgZone } from '@angular/core';
import { BehaviorSubject, fromEvent, merge, timer, Subject } from 'rxjs';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { Router } from '@angular/router';
import { AuthService } from '../auth/services/auth.service';

@Injectable({
    providedIn: 'root'
})
export class SessionService {
    private readonly INACTIVITY_TIMEOUT = 15 * 60 * 1000; // 15 minutes
    private readonly WARNING_TIME = 13 * 60 * 1000; // 13 minutes (2 min warning)

    private lastActivityTime: number = Date.now();
    private inactivityTimer: any;
    private warningTimer: any;
    private destroy$ = new Subject<void>();

    public showWarning$ = new BehaviorSubject<boolean>(false);
    public timeRemaining$ = new BehaviorSubject<number>(0);

    constructor(
        private ngZone: NgZone,
        private router: Router,
        private authService: AuthService
    ) { }

    startTracking(): void {
        if (typeof window === 'undefined') return;

        // Track user activity
        this.ngZone.runOutsideAngular(() => {
            merge(
                fromEvent(document, 'mousemove'),
                fromEvent(document, 'keydown'),
                fromEvent(document, 'click'),
                fromEvent(document, 'scroll'),
                fromEvent(document, 'touchstart')
            )
                .pipe(
                    debounceTime(1000),
                    takeUntil(this.destroy$)
                )
                .subscribe(() => {
                    this.ngZone.run(() => this.resetTimers());
                });
        });

        this.resetTimers();
    }

    private resetTimers(): void {
        this.lastActivityTime = Date.now();
        this.showWarning$.next(false);

        // Clear existing timers
        if (this.warningTimer) clearTimeout(this.warningTimer);
        if (this.inactivityTimer) clearTimeout(this.inactivityTimer);

        // Set warning timer (13 minutes)
        this.warningTimer = setTimeout(() => {
            this.showWarning$.next(true);
            this.startCountdown();
        }, this.WARNING_TIME);

        // Set logout timer (15 minutes)
        this.inactivityTimer = setTimeout(() => {
            this.logout();
        }, this.INACTIVITY_TIMEOUT);
    }

    private startCountdown(): void {
        const countdownInterval = setInterval(() => {
            const elapsed = Date.now() - this.lastActivityTime;
            const remaining = Math.max(0, this.INACTIVITY_TIMEOUT - elapsed);

            this.timeRemaining$.next(Math.floor(remaining / 1000));

            if (remaining <= 0) {
                clearInterval(countdownInterval);
            }
        }, 1000);
    }

    extendSession(): void {
        this.resetTimers();
        // Optionally refresh the JWT token here
        this.authService.refreshToken().subscribe({
            next: () => console.log('Session extended'),
            error: (err) => console.error('Failed to extend session:', err)
        });
    }

    private logout(): void {
        this.showWarning$.next(false);
        this.authService.logout();
        this.router.navigate(['/auth/login'], {
            queryParams: { reason: 'session_timeout' }
        });
    }

    stopTracking(): void {
        this.destroy$.next();
        this.destroy$.complete();
        if (this.warningTimer) clearTimeout(this.warningTimer);
        if (this.inactivityTimer) clearTimeout(this.inactivityTimer);
    }
}
