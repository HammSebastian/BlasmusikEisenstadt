import {Component, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../core/services/essentials/auth.service';
import {UserModel} from '../../core/models/essentials/user.model';
import {RoleEnum} from '../../core/models/essentials/role.enum';

;

@Component({
    selector: 'app-auth',
    imports: [
        FormsModule,
        ReactiveFormsModule
    ],
    templateUrl: './auth.html',
    styleUrl: './auth.css'
})
export class Auth implements OnInit {
    private readonly fb = inject(FormBuilder);
    private readonly authService = inject(AuthService);
    private readonly router = inject(Router);
    private readonly route = inject(ActivatedRoute);

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

    ngOnInit() {
        //check if user is already logged in
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/member/dashboard']);
        }
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
                const mockUser: UserModel = {
                    id: '1',
                    email,
                    name: 'Demo User',
                    role: RoleEnum.Admin,
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
