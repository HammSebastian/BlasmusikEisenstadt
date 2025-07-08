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
            username: ['', [Validators.required]],
            password: ['', [Validators.required]],
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

            const loginPayload = {
                username: this.loginForm.value.username,
                password: this.loginForm.value.password
            };


            this.authService.login(loginPayload).subscribe({
                next: (response) => {
                    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/member/dashboard';
                    this.router.navigate([returnUrl]);

                    this.isLoading.set(false);
                },
                error: (error) => {
                    console.error('Login failed:', error);
                    // Hier kannst du eine Fehleranzeige ergänzen
                    this.isLoading.set(false);
                }
            });
        }
    }

}
