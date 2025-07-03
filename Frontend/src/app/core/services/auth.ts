import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

export interface User {
    id: string;
    email: string;
    name: string;
    role: string;
}

export interface LoginCredentials {
    email: string;
    password: string;
}

export interface AuthResponse {
    user: User;
    token: string;
    refreshToken: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private http = inject(HttpClient);
    private router = inject(Router);

    private readonly TOKEN_KEY = 'blasmusik_token';
    private readonly REFRESH_TOKEN_KEY = 'blasmusik_refresh_token';
    private readonly USER_KEY = 'blasmusik_user';

    private currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());
    public currentUser$ = this.currentUserSubject.asObservable();

    // Signals for reactive state management
    private authState = signal<{
        user: User | null;
        isAuthenticated: boolean;
        isLoading: boolean;
    }>({
        user: this.getUserFromStorage(),
        isAuthenticated: !!this.getToken(),
        isLoading: false
    });

    // Computed signals
    public isAuthenticated = computed(() => this.authState().isAuthenticated);
    public currentUser = computed(() => this.authState().user);
    public isLoading = computed(() => this.authState().isLoading);

    constructor() {
        // Check if user is already logged in on app start
        const token = this.getToken();
        const user = this.getUserFromStorage();

        if (token && user) {
            this.updateAuthState(user, true);
        }
    }

    login(credentials: LoginCredentials): Observable<AuthResponse> {
        this.setLoading(true);

        return this.http.post<AuthResponse>('/api/auth/login', credentials).pipe(
            tap(response => {
                this.setAuthData(response);
                this.updateAuthState(response.user, true);
                this.router.navigate(['/home']);
            }),
            catchError(error => {
                this.setLoading(false);
                return throwError(() => error);
            })
        );
    }

    logout(): void {
        this.clearAuthData();
        this.updateAuthState(null, false);
        this.router.navigate(['/auth/login']);
    }

    refreshToken(): Observable<AuthResponse> {
        const refreshToken = this.getRefreshToken();

        if (!refreshToken) {
            this.logout();
            return throwError(() => new Error('No refresh token available'));
        }

        return this.http.post<AuthResponse>('/api/auth/refresh', { refreshToken }).pipe(
            tap(response => {
                this.setAuthData(response);
                this.updateAuthState(response.user, true);
            }),
            catchError(error => {
                this.logout();
                return throwError(() => error);
            })
        );
    }

    getToken(): string | null {
        return localStorage.getItem(this.TOKEN_KEY);
    }

    private getRefreshToken(): string | null {
        return localStorage.getItem(this.REFRESH_TOKEN_KEY);
    }

    private getUserFromStorage(): User | null {
        const userStr = localStorage.getItem(this.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    }

    private setAuthData(response: AuthResponse): void {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
        this.currentUserSubject.next(response.user);
    }

    private clearAuthData(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        this.currentUserSubject.next(null);
    }

    private updateAuthState(user: User | null, isAuthenticated: boolean): void {
        this.authState.update(state => ({
            ...state,
            user,
            isAuthenticated,
            isLoading: false
        }));
    }

    private setLoading(isLoading: boolean): void {
        this.authState.update(state => ({
            ...state,
            isLoading
        }));
    }
}
