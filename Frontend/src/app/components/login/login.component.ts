import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {LoginRequest} from '../../models/auth.model';
import {CommonModule} from '@angular/common';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    errorMessage: string = '';
    isLoading: boolean = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router
    ) {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required]],
            password: ['', [Validators.required]]
        });
    }

    ngOnInit(): void {
        if (this.authService.isAuthenticated()) {
            // Optional: Redirect if already logged in
            // this.router.navigate(['/admin']); // or some dashboard
        }
    }

    onSubmit(): void {
        if (this.loginForm.invalid) {
            this.errorMessage = 'Please fill in all fields.';
            return;
        }

        this.isLoading = true;
        this.errorMessage = '';
        const credentials: LoginRequest = this.loginForm.value;

        this.authService.login(credentials).subscribe({
            next: (response) => {
                this.isLoading = false;
                // Navigate to a protected route or dashboard
                this.router.navigate(['/']); // Or '/admin', '/dashboard' etc.
                console.log('Login successful', response);
            },
            error: (err) => {
                this.isLoading = false;
                this.errorMessage = 'Login failed. Please check your credentials and try again.';
                if (err.error && typeof err.error === 'string') {
                    this.errorMessage = err.error;
                } else if (err.status === 0) {
                    this.errorMessage = 'Could not connect to server. Please try again later.';
                }
                console.error('Login error', err);
            }
        });
    }
}
