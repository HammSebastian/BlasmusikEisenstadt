import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { UserModel } from '../../models/essentials/user.model';
import { AuthResponse } from '../../models/essentials/authResponse.model';
import { LoginCredentials } from '../../models/essentials/loginCredentials.model';
import { environment } from '../../../../environment/environment';
import { RoleEnum } from '../../models/essentials/role.enum';
import {ApiResponse} from '../../models/essentials/apiResponse.model';
import {ProfileResponse} from '../../models/private/profile.response';

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

    private readonly authState = signal<{
        user: UserModel | null;
        isAuthenticated: boolean;
        isLoading: boolean;
    }>({
        user: this.getUserFromStorage(),
        isAuthenticated: !!this.getToken(),
        isLoading: false
    });

    public isAuthenticated = computed(() => this.authState().isAuthenticated);
    public currentUser = computed(() => this.authState().user);
    public isAdmin = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.ADMIN) || roles.includes(RoleEnum.REPORTER);
    });

    public canManageGigs = computed(() => {
        const roles = this.authState().user?.roles || [];
        return roles.includes(RoleEnum.ADMIN) || roles.includes(RoleEnum.REPORTER) || roles.includes(RoleEnum.CONDUCTOR);
    });

    constructor() {}

    login(credentials: LoginCredentials): Observable<AuthResponse> {
        this.setLoading(true);

        return this.http.post<AuthResponse>(this.baseUrl + '/auth/login', credentials).pipe(
            tap(response => {
                this.setAuthData(response);
                this.updateAuthState(response.user, true);
                this.router.navigate(['/member/dashboard']);
            }),
            catchError(error => {
                this.setLoading(false);
                return throwError(() => error);
            })
        );
    }

    logout(): void {
        this.http.post(this.baseUrl + '/auth/logout', {}).subscribe(() => {
            this.clearAuthData();
            this.updateAuthState(null, false);
            this.router.navigate(['/']);
        });
    }

    getToken(): string | null {
        if (typeof localStorage === 'undefined') return null;
        return localStorage.getItem(this.TOKEN_KEY);
    }

    private getUserFromStorage(): UserModel | null {
        if (typeof localStorage === 'undefined') return null;
        const userStr = localStorage.getItem(this.USER_KEY);
        if (!userStr) return null;

        try {
            return JSON.parse(userStr);
        } catch {
            // Falls kein gültiges JSON, lösche falschen Wert, gib null zurück
            localStorage.removeItem(this.USER_KEY);
            return null;
        }
    }

    private setAuthData(response: AuthResponse): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
        localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
    }

    private clearAuthData(): void {
        if (typeof localStorage === 'undefined') return;
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem(this.REFRESH_TOKEN_KEY);
        localStorage.removeItem(this.USER_KEY);
    }

    private updateAuthState(user: UserModel | null, isAuthenticated: boolean): void {
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

    fetchUserProfile(): Observable<ProfileResponse> {
        this.setLoading(true);

        return this.http.get<ProfileResponse>(this.baseUrl + '/profile').pipe(
            tap(response => {

                const rolesFromBackend: string[] = response.roles; // z.B. ["ROLE_ADMIN", "ROLE_USER"]

                const rolesMapped: RoleEnum[] = rolesFromBackend.map(roleStr => {
                    switch(roleStr) {
                        case 'ROLE_ADMIN': return RoleEnum.ADMIN;
                        case 'ROLE_REPORTER': return RoleEnum.REPORTER;
                        case 'ROLE_CONDUCTOR': return RoleEnum.CONDUCTOR;
                        default: throw new Error(`Unknown role: ${roleStr}`);
                    }
                });
                const user: UserModel = {
                    id: response.id,
                    name: response.username,  // oder wie es heißt
                    email: response.email,
                    roles: rolesMapped,    // falls roles vorhanden
                };
                this.updateAuthState(user, true);
                this.setLoading(false);
            }),
            catchError(error => {
                this.setLoading(false);
                return throwError(() => error);
            })
        );
    }


}
