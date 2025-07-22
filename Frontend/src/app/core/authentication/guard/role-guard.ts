import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '@auth0/auth0-angular';
import { catchError, map, switchMap, take } from 'rxjs/operators';
import { from, Observable, of } from 'rxjs';
import {TokenService} from "../service/token.service";

export const roleGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
    const authService = inject(AuthService);
    const router = inject(Router);
    const tokenService = inject(TokenService);

    const requiredRoles = route.data['roles'] as string[] || [];
    const requiredPermissions = route.data['permissions'] as string[] || [];

    if (requiredRoles.length === 0 && requiredPermissions.length === 0) {
        return of(true);
    }

    return authService.isAuthenticated$.pipe(
        take(1),
        switchMap(isAuthenticated => {
            if (!isAuthenticated) {
                return from(authService.loginWithRedirect({
                    appState: { target: state.url },
                    authorizationParams: {
                        audience: 'https://api.stadtkapelle-eisenstadt.at/',
                        scope: 'openid profile email ' + requiredPermissions.join(' ')
                    }
                })).pipe(
                    map(() => false),
                    catchError(() => of(false))
                );
            }

            return authService.getAccessTokenSilently({
                authorizationParams: {
                    audience: 'https://api.stadtkapelle-eisenstadt.at/',
                    scope: 'openid profile email ' + requiredPermissions.join(' ')
                }
            }).pipe(
                map(token => {
                    const permissions = tokenService.extractPermissionsFromToken(token);

                    const hasRequiredPermission = requiredPermissions.length === 0 ||
                        requiredPermissions.some(permission => permissions.includes(permission));

                    const userRoles = tokenService.mapPermissionsToRoles(permissions);

                    const hasRequiredRole = requiredRoles.length === 0 ||
                        requiredRoles.some(role => userRoles.includes(role.toLowerCase()));

                    if (hasRequiredPermission || hasRequiredRole) {
                        return true;
                    } else {
                        return router.parseUrl('/private/user/profile');
                    }
                }),
                catchError(error => {
                    if (error.error === 'consent_required' || error.error === 'login_required') {
                        return from(authService.loginWithRedirect({
                            appState: { target: state.url },
                            authorizationParams: {
                                audience: 'https://api.stadtkapelle-eisenstadt.at/',
                                scope: 'openid profile email ' + requiredPermissions.join(' ')
                            }
                        })).pipe(
                            map(() => false),
                            catchError(() => of(false))
                        );
                    } else {
                        return of(router.parseUrl('/'));
                    }
                })
            );
        })
    );
};
