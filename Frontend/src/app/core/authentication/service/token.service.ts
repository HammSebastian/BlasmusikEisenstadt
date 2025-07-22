import {Injectable} from '@angular/core';

/**
 * Service responsible for token-related operations
 * Follows Single Responsibility Principle by focusing only on token handling
 */
@Injectable({
    providedIn: 'root'
})
export class TokenService {
    /**
     * Extracts permissions from a JWT token
     * @param token The JWT token
     * @returns Array of permission strings
     */
    extractPermissionsFromToken(token: string): string[] {
        if (!token) return [];

        try {
            const base64Url = token.split('.')[1];
            if (!base64Url) return [];

            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const decodedPayload = JSON.parse(atob(base64));

            return Array.isArray(decodedPayload.permissions)
                ? decodedPayload.permissions
                : [];
        } catch (error) {
            console.error('TokenService: Error decoding token:', error);
            return [];
        }
    }

    /**
     * Maps permissions to user roles
     * @param permissions Array of permission strings
     * @returns Array of role strings
     */
    mapPermissionsToRoles(permissions: string[]): string[] {
        const roles: string[] = [];

        // Map permissions to roles
        if (permissions.includes('read:admin') || permissions.includes('write:admin')) {
            roles.push('admin');
        }

        if (permissions.includes('read:conductor') || permissions.includes('write:conductor')) {
            roles.push('conductor');
        }

        if (permissions.includes('read:sectionleader') || permissions.includes('write:sectionleader')) {
            roles.push('sectionleader');
        }

        if (permissions.includes('read:reporter') || permissions.includes('write:reporter')) {
            roles.push('reporter');
        }

        if (permissions.includes('read:musician') || permissions.includes('write:musician')) {
            roles.push('musician');
        }

        // If no specific role is found, assign a default authenticated user role
        if (roles.length === 0) {
            roles.push('user');
        }

        return [...new Set(roles)]; // Remove duplicates
    }

    /**
     * Gets user-friendly role names for display
     * @param roles Array of role strings
     * @returns Array of user-friendly role names
     */
    getRoleDisplayNames(roles: string[]): string[] {
        const displayNames: Record<string, string> = {
            'admin': 'Administrator',
            'conductor': 'Kapellmeister',
            'sectionleader': 'Registerführer',
            'reporter': 'Reporter',
            'musician': 'Musiker',
            'user': 'Authentifizierter Benutzer'
        };

        return roles.map(role => displayNames[role] || role);
    }
}
