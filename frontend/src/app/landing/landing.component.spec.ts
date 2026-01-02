import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, it, expect, beforeEach, afterEach, vi, beforeAll } from 'vitest';
import { LandingComponent } from './landing.component';
import { RouterTestingModule } from '@angular/router/testing';
import { PLATFORM_ID } from '@angular/core';

// Mock IntersectionObserver
class MockIntersectionObserver {
    observe = vi.fn();
    unobserve = vi.fn();
    disconnect = vi.fn();
}

beforeAll(() => {
    (window as any).IntersectionObserver = MockIntersectionObserver;
});

describe('LandingComponent', () => {
    let component: LandingComponent;
    let fixture: ComponentFixture<LandingComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [LandingComponent, RouterTestingModule],
            providers: [
                { provide: PLATFORM_ID, useValue: 'browser' }
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(LandingComponent);
        component = fixture.componentInstance;
    });

    afterEach(() => {
        component.ngOnDestroy();
    });

    describe('Component Initialization', () => {
        it('should create the component', () => {
            expect(component).toBeTruthy();
        });

        it('should initialize with current year', () => {
            expect(component.currentYear).toBe(new Date().getFullYear());
        });

        it('should initialize with dark mode off', () => {
            expect(component.isDarkMode).toBe(false);
        });

        it('should initialize with default time 9:41', () => {
            expect(component.currentTime).toBe('9:41');
        });

        it('should have mock notifications array with 3 items', () => {
            expect(component.notifications.length).toBe(3);
        });

        it('should start with first notification (index 0)', () => {
            expect(component.currentNotificationIndex).toBe(0);
        });

        it('should have showNotification as true', () => {
            expect(component.showNotification).toBe(true);
        });
    });

    describe('Counter Animation', () => {
        it('should initialize discussion count with base value', () => {
            expect(component.discussionCount).toBeGreaterThanOrEqual(10000);
        });

        it('should initialize member count with base value', () => {
            expect(component.memberCount).toBeGreaterThanOrEqual(10000);
        });

        it('should initialize solution count with base value', () => {
            expect(component.solutionCount).toBeGreaterThanOrEqual(7000);
        });
    });

    describe('Dark Mode Toggle', () => {
        beforeEach(() => {
            vi.spyOn(Storage.prototype, 'setItem');
        });

        it('should toggle dark mode from false to true', () => {
            component.isDarkMode = false;
            component.toggleDarkMode();
            expect(component.isDarkMode).toBe(true);
        });

        it('should toggle dark mode from true to false', () => {
            component.isDarkMode = true;
            component.toggleDarkMode();
            expect(component.isDarkMode).toBe(false);
        });

        it('should save dark theme to localStorage when enabling', () => {
            component.isDarkMode = false;
            component.toggleDarkMode();
            expect(localStorage.setItem).toHaveBeenCalledWith('theme', 'dark');
        });

        it('should save light theme to localStorage when disabling', () => {
            component.isDarkMode = true;
            component.toggleDarkMode();
            expect(localStorage.setItem).toHaveBeenCalledWith('theme', 'light');
        });
    });

    describe('Current Notification Getter', () => {
        it('should return the first notification when index is 0', () => {
            component.currentNotificationIndex = 0;
            expect(component.currentNotification).toEqual(component.notifications[0]);
        });

        it('should return correct notification for each index', () => {
            for (let i = 0; i < component.notifications.length; i++) {
                component.currentNotificationIndex = i;
                expect(component.currentNotification).toEqual(component.notifications[i]);
            }
        });
    });

    describe('Format Number', () => {
        it('should format numbers >= 1000 with K suffix', () => {
            expect(component.formatNumber(1000)).toBe('1.0K');
            expect(component.formatNumber(12000)).toBe('12.0K');
            expect(component.formatNumber(12500)).toBe('12.5K');
        });

        it('should return number as string for values < 1000', () => {
            expect(component.formatNumber(500)).toBe('500');
            expect(component.formatNumber(999)).toBe('999');
        });
    });

    describe('Template Rendering', () => {
        it('should create component without errors', () => {
            expect(component).toBeTruthy();
            expect(component.notifications.length).toBe(3);
            expect(typeof component.formatNumber).toBe('function');
        });
    });
});
