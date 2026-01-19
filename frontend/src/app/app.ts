import { Component, signal, inject, PLATFORM_ID, OnInit, OnDestroy } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { UsersService } from './services/users.service';
import { SessionService } from './services/session.service';
import { AuthService } from './auth/services/auth.service';
import { SessionTimeoutModalComponent } from './components/session-timeout-modal/session-timeout-modal.component';
import { User } from '../app/models/user.model'


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SessionTimeoutModalComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit, OnDestroy {
  protected readonly title = signal('frontend');
  users: User[] = [];
  private platformId = inject(PLATFORM_ID);

  constructor(
    private usersService: UsersService,
    private sessionService: SessionService,
    private authService: AuthService
  ) {
    // Only load users in browser, not on server
    if (isPlatformBrowser(this.platformId)) {
      this.loadUsers();
    }
  }

  ngOnInit(): void {
    // Start session tracking if user is authenticated
    if (isPlatformBrowser(this.platformId)) {
      this.authService.isAuthenticated$.subscribe(isAuth => {
        if (isAuth) {
          this.sessionService.startTracking();
        } else {
          this.sessionService.stopTracking();
        }
      });
    }
  }

  ngOnDestroy(): void {
    this.sessionService.stopTracking();
  }

  loadUsers() {
    this.usersService.getAllUsers().subscribe(
      {
        next: (data) => {
          this.users = data;
          console.log('Users loaded: ', this.users)
        },
        error: (err) => console.error('Error loading users: ', err)
      }
    );
  }
}



