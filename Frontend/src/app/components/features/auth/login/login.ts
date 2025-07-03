import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import {AuthService} from '../../../../core/services/auth';
import {SeoService} from '../../../../core/services/seo';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login implements OnInit {
    private fb = inject(FormBuilder);
    private authService = inject(AuthService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);
    private seoService = inject(SeoService);

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

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Login - Blasmusik',
            description: 'Sign in to your Blasmusik account to access your brass band management dashboard.',
            keywords: 'login, sign in, brass band, authentication'
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

            const { email, password } = this.loginForm.value;

            // Mock login - in real app this would call the auth service
            setTimeout(() => {
                // Simulate successful login
                const mockResponse = {
                    user: {
                        id: '1',
                        email,
                        name: 'Demo User',
                        role: 'admin'
                    },
                    token: 'mock-jwt-token',
                    refreshToken: 'mock-refresh-token'
                };

                // Store auth data manually for demo
                localStorage.setItem('blasmusik_token', mockResponse.token);
                localStorage.setItem('blasmusik_refresh_token', mockResponse.refreshToken);
                localStorage.setItem('blasmusik_user', JSON.stringify(mockResponse.user));

                // Update auth service state
                this.authService['updateAuthState'](mockResponse.user, true);

                // Get return URL or default to home
                const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
                this.router.navigate([returnUrl]);

                this.isLoading.set(false);
            }, 1500);
        }
    }
}
