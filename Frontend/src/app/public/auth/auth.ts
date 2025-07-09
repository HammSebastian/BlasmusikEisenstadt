import {Component, inject, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../core/services/essentials/auth.service';
import {UserModel} from '../../core/models/essentials/user.model';
import {RoleEnum} from '../../core/models/essentials/role.enum';

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
    errorMessage = signal<string>('');

    constructor() {
        this.loginForm = this.fb.group({
            username: ['Admin', [Validators.required]],
            password: ['MMBwQjz5LBcSZdBRdWep9Wd4kM5GZB75', [Validators.required]],
            remember: [false]
        });
    }

    ngOnInit() {
        if (this.authService.isAuthenticated()) {
            this.router.navigate(['/musician/dashboard']);
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
            this.errorMessage.set('');

            const loginPayload = {
                username: this.loginForm.value.username,
                password: this.loginForm.value.password
            };


            this.authService.login(loginPayload).subscribe({
                next: (response) => {
                    this.isLoading.set(false);
                },
                error: (error) => {
                    this.isLoading.set(false);

                    // Use the error message from the server if available
                    const errorMessage = error.message || error.error?.message;

                    if (errorMessage) {
                        this.errorMessage.set(errorMessage);
                    } else if (error.status === 401) {
                        this.errorMessage.set('Ungültiger Benutzername oder Passwort. Bitte versuchen Sie es erneut.');
                    } else if (error.status === 403) {
                        this.errorMessage.set('Zugriff verweigert. Sie haben keine Berechtigung für diese Aktion.');
                    } else if (error.status === 0) {
                        this.errorMessage.set('Verbindungsfehler. Bitte überprüfen Sie Ihre Internetverbindung.');
                    } else {
                        this.errorMessage.set('Ein unbekannter Fehler ist aufgetreten. Bitte versuchen Sie es später erneut.');
                    }
                }
            });
        }
    }
}
