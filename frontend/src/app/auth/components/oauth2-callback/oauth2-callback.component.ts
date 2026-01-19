import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-oauth2-callback',
    standalone: true,
    imports: [CommonModule],
    template: `
    <div class="flex items-center justify-center min-h-screen bg-gray-100">
      <div class="text-center">
        <h2 class="text-2xl font-bold mb-4">Processing Login...</h2>
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary mx-auto"></div>
        <p class="mt-4 text-gray-600">Please wait while we redirect you.</p>
      </div>
    </div>
  `,
    styles: []
})
export class OAuth2CallbackComponent implements OnInit {

    constructor(
        private route: ActivatedRoute,
        private authService: AuthService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            const token = params['token'];
            const expiresAt = params['expiresAt'];

            if (token && expiresAt) {
                this.authService.handleOAuthCallback(token, parseInt(expiresAt));
            } else {
                // Error or missing params, redirect to login
                this.router.navigate(['/auth/login'], { queryParams: { error: 'OAuth2MissingParams' } });
            }
        });
    }
}
