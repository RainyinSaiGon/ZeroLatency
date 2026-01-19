import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../services/session.service';

@Component({
    selector: 'app-session-timeout-modal',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="modal-overlay" *ngIf="sessionService.showWarning$ | async">
      <div class="modal-content">
        <div class="modal-icon">⏰</div>
        <h2>Session Expiring Soon</h2>
        <p>Your session will expire in <strong>{{ timeRemaining }}</strong> seconds due to inactivity.</p>
        <p class="modal-subtitle">Click below to stay logged in.</p>
        
        <div class="modal-actions">
          <button class="btn-primary" (click)="extendSession()">
            Stay Logged In
          </button>
        </div>
      </div>
    </div>
  `,
    styles: [`
    .modal-overlay {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(15, 23, 42, 0.75);
      backdrop-filter: blur(4px);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9999;
      animation: fadeIn 0.2s ease;
    }

    @keyframes fadeIn {
      from { opacity: 0; }
      to { opacity: 1; }
    }

    .modal-content {
      background: white;
      padding: 40px;
      border-radius: 16px;
      max-width: 420px;
      width: 90%;
      text-align: center;
      box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
      animation: slideUp 0.3s ease;
    }

    @keyframes slideUp {
      from {
        opacity: 0;
        transform: translateY(20px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    .modal-icon {
      font-size: 64px;
      margin-bottom: 20px;
      animation: pulse 2s ease-in-out infinite;
    }

    @keyframes pulse {
      0%, 100% { transform: scale(1); }
      50% { transform: scale(1.1); }
    }

    h2 {
      font-size: 24px;
      font-weight: 700;
      color: #0F172A;
      margin-bottom: 12px;
    }

    p {
      font-size: 15px;
      color: #64748B;
      line-height: 1.6;
      margin-bottom: 8px;
    }

    p strong {
      color: #EF4444;
      font-weight: 700;
      font-size: 18px;
    }

    .modal-subtitle {
      margin-bottom: 24px;
    }

    .modal-actions {
      display: flex;
      gap: 12px;
      justify-content: center;
    }

    .btn-primary {
      background: #0F766E;
      color: white;
      border: none;
      padding: 14px 32px;
      border-radius: 8px;
      font-size: 15px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.2s ease;
      font-family: 'Outfit', 'Inter', sans-serif;
    }

    .btn-primary:hover {
      background: #0D5D56;
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(15, 118, 110, 0.3);
    }
  `]
})
export class SessionTimeoutModalComponent {
    constructor(public sessionService: SessionService) { }

    get timeRemaining(): number {
        return this.sessionService.timeRemaining$.value;
    }

    extendSession(): void {
        this.sessionService.extendSession();
    }
}
