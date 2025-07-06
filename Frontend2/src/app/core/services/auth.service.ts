import {Injectable, inject, signal, computed} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, BehaviorSubject, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import { User } from '../models/generell/user.model';
import {LoginCredentials} from '../models/generell/LoginCredentials.model';
import {AuthResponse} from '../models/generell/AuthResponse.model';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);

    private readonly TOKEN_KEY = 'blasmusik_token';
    private readonly REFRESH_TOKEN_KEY = 'blasmusik_refresh_token';
    private readonly USER_KEY = 'blasmusik_user';

    private readonly currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromStorage());

    // Signals for reactive state management
    private readonly authState = signal<{
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
    public isAdmin = computed(() => {
        const user = this.authState().user;
        return user?.role === 'admin' || user?.role === 'reporter';
    });
    public canManageGigs = computed(() => {
        const user = this.authState().user;
        return user?.role === 'admin' || user?.role === 'reporter' || user?.role === 'conductor';
    });

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
                this.router.navigate(['/dashboard']);
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
        this.router.navigate(['/public/home']);
    }

    getToken(): string | null {
        if (typeof localStorage === 'undefined') return null;
        return localStorage.getItem(this.TOKEN_KEY);
    }

    private getUserFromStorage(): User | null {
        if (typeof localStorage === 'undefined') return null;
        const userStr = localStorage.getItem(this.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    }

    private setAuthData(response: AuthResponse): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
        this.currentUserSubject.next(response.user);
    }

    private clearAuthData(): void {
        if (typeof localStorage === 'undefined') return;
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
