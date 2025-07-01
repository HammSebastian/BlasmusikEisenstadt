import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/auth.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      // confirmPassword: ['', Validators.required] // Add custom validator for matching passwords
    }
    // , { validator: this.passwordMatchValidator } // Example for custom validator
    );
  }

  ngOnInit(): void {}

  // Custom validator example (optional)
  // passwordMatchValidator(form: FormGroup) {
  //   return form.get('password')?.value === form.get('confirmPassword')?.value
  //     ? null : { mismatch: true };
  // }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.errorMessage = 'Please correct the errors in the form.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    const userInfo: RegisterRequest = this.registerForm.value;
    // Default role to USER for self-registration, admin can change later if needed
    userInfo.role = 'USER';


    this.authService.register(userInfo).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Registration successful! You can now login.'; // Or response message
        console.log('Registration successful', response);
        // Optionally redirect to login or show a success message
        // this.router.navigate(['/login']);
        this.registerForm.reset();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Registration failed. Please try again.';
         if (err.error && typeof err.error === 'string') {
            this.errorMessage = err.error;
        } else if (err.status === 0) {
            this.errorMessage = 'Could not connect to server. Please try again later.';
        }
        console.error('Registration error', err);
      }
    });
  }
}
