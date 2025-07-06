import {Injectable, inject, signal, computed} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {Observable, throwError} from 'rxjs'; // BehaviorSubject entfernt
import {catchError, tap} from 'rxjs/operators';
import {UserModel} from '../../models/essentials/user.model';
import {AuthResponse} from '../../models/essentials/authResponse.model';
import {LoginCredentials} from '../../models/essentials/loginCredentials.model';
import {environment} from '../../../../environment/environment';


@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);

    private readonly TOKEN_KEY = 'blasmusik_token';
    private readonly REFRESH_TOKEN_KEY = 'blasmusik_refresh_token';
    private readonly USER_KEY = 'blasmusik_user';

    private readonly baseUrl = environment.apiUrl;

    // Signals for reactive state management
    // Initialisierung des Zustands direkt aus dem Storage
    private readonly authState = signal<{
        user: UserModel | null;
        isAuthenticated: boolean;
        isLoading: boolean;
    }>({
        user: this.getUserFromStorage(),
        isAuthenticated: !!this.getToken(), // Prüft, ob ein Token vorhanden ist
        isLoading: false
    });

    // Computed signals (öffentlich für den Zugriff von außen)
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

    // Der Konstruktor kann leer bleiben, da die Initialisierung bei der Deklaration erfolgt
    constructor() {
    }

    login(credentials: LoginCredentials): Observable<AuthResponse> {
        this.setLoading(true); // Setzt isLoading auf true

        return this.http.post<AuthResponse>(this.baseUrl + '/auth/login', credentials).pipe(
            tap(response => {
                this.setAuthData(response); // Speichert Daten im Storage
                this.updateAuthState(response.user, true); // Aktualisiert den Signal-Zustand
                this.router.navigate(['/member/dashboard']);
            }),
            catchError(error => {
                this.setLoading(false); // Setzt isLoading auf false bei Fehler
                return throwError(() => error);
            })
        );
    }

    logout(): void {
        this.clearAuthData(); // Löscht Daten aus dem Storage
        this.updateAuthState(null, false); // Setzt den Signal-Zustand zurück
        this.router.navigate(['/']);
    }

    getToken(): string | null {
        if (typeof localStorage === 'undefined') return null;
        return localStorage.getItem(this.TOKEN_KEY);
    }

    private getUserFromStorage(): UserModel | null {
        if (typeof localStorage === 'undefined') return null;
        const userStr = localStorage.getItem(this.USER_KEY);
        return userStr ? JSON.parse(userStr) : null;
    }

    private setAuthData(response: AuthResponse): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
        // currentUserSubject.next(response.user); // Diese Zeile wurde entfernt
    }

    private clearAuthData(): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
        // currentUserSubject.next(null); // Diese Zeile wurde entfernt
    }

    private updateAuthState(user: UserModel | null, isAuthenticated: boolean): void {
        this.authState.update(state => ({
            ...state,
            user,
            isAuthenticated,
            isLoading: false // isLoading wird hier zurückgesetzt
        }));
    }

    private setLoading(isLoading: boolean): void {
        this.authState.update(state => ({
            ...state,
            isLoading
        }));
    }
}
