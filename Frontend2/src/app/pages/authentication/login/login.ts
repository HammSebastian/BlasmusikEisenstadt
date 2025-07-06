import {Component, inject, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from '../../../core/services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SeoService} from '../../../core/services/seo.service';
import {User} from '../../../core/models/generell/user.model';

@Component({
    selector: 'app-login',
    imports: [
        FormsModule,
        ReactiveFormsModule
    ],
    templateUrl: './login.html',
    styleUrl: './login.css'
})
export class Login {
    private readonly fb = inject(FormBuilder);
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly route = inject(ActivatedRoute);
    private readonly seoService = inject(SeoService);

    showPassword = signal(false);
    isLoading = signal(false);

    loginForm: FormGroup;

    constructor() {
        this.loginForm = this.fb.group({
            email: ['demo@blasmusik.com', [Validators.required, Validators.email]],
            password: ['demo123', [Validators.required]],
            remember: [false]
        });
    }

    protected togglePassword(): void {
        this.showPassword.update(show => !show);
    }

    protected isFieldInvalid(fieldName: string): boolean {
        const field = this.loginForm.get(fieldName);
        return !!(field && field.invalid && (field.dirty || field.touched));
    }

    protected onSubmit(): void {
        if (this.loginForm.valid) {
            this.isLoading.set(true);

            const {email, password} = this.loginForm.value;

            // Mock login - in real app this would call the auth service
            setTimeout(() => {
                // Simulate successful login
                const mockUser: User = {
                    id: '1',
                    email,
                    name: 'Demo User',
                    role: 'admin',
                    status: 'active',
                    lastLogin: 'Yesterday',
                    joinDate: 'Yesterday'
                };

                const mockResponse = {
                    user: mockUser,
                    token: 'mock-jwt-token',
                    refreshToken: 'mock-refresh-token'
                };

                // Store auth data manually for demo
                localStorage.setItem('blasmusik_token', mockResponse.token);
                localStorage.setItem('blasmusik_refresh_token', mockResponse.refreshToken);
                localStorage.setItem('blasmusik_user', JSON.stringify(mockResponse.user));

                // Update auth service state
                (this.authService as any)['updateAuthState'](mockResponse.user, true);

                // Get return URL or default to dashboard
                const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/member-dashboard';
                this.router.navigate([returnUrl]);

                this.isLoading.set(false);
            }, 1500);
        }
    }
}
