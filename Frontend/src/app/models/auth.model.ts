// Basic models for authentication data transfer

export interface LoginRequest {
    username?: string;
    password?: string;
}

export interface RegisterRequest {
    username?: string;
    email?: string;
    password?: string;
    role?: string; // 'ADMIN', 'EDITOR', 'USER'
}

export interface AuthResponse {
    token: string;
    tokenType?: string; // Typically 'Bearer'
    userId: number;
    username: string;
    email: string;
    role: string; // Role name as string
}

export interface User {
    id: number;
    username: string;
    email: string;
    role: string;
    enabled?: boolean;
    notificationPreferences?: string; // JSON string
}
