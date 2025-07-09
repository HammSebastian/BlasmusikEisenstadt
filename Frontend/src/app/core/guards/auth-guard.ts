import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import { map, Observable, of, switchMap } from 'rxjs';
import { AuthService } from '../services/essentials/auth.service';

export const authGuard: CanActivateFn = (route, state): Observable<boolean | UrlTree> => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // Redirect to login if not authenticated
    if (!authService.isLoggedIn()) {
        return of(router.createUrlTree(['/login'], { 
            queryParams: { returnUrl: state.url } 
        }));
    }

    // Get the required roles from the route data
    const requiredRoles = route.data?.['roles'] as string[];
    
    // If no specific roles required, allow access
    if (!requiredRoles || requiredRoles.length === 0) {
        return of(true);
    }

    // Check if user has any of the required roles
    const hasRequiredRole = requiredRoles.some(role => {
        switch (role) {
            case 'ADMIN': return authService.isLoggedInAsAdmin();
            case 'REPORTER': return authService.isLoggedInAsReporter();
            case 'CONDUCTOR': return authService.isLoggedInAsConductor();
            case 'SECTION_LEADER': return authService.isLoggedInAsSectionLeader();
            case 'MUSICIAN': return authService.isLoggedInAsMusician();
            default: return false;
        }
    });

    if (hasRequiredRole) {
        return of(true);
    }

    // If user doesn't have required role, redirect to unauthorized or home
    console.warn(`Access denied. Required roles: ${requiredRoles.join(', ')}`);
    return of(router.createUrlTree(['/unauthorized'])); // Make sure you have an unauthorized route
};

// Role-based guard that can be used directly in route definitions
export const roleGuard = (roles: string[]): CanActivateFn => {
    return (route, state) => {
        const authService = inject(AuthService);
        const router = inject(Router);

        // Add the roles to the route data
        route.data = { ...route.data, roles };
        
        // Use the main auth guard with the updated route data
        return authGuard(route, state);
    };
};
