import { Component, computed, DestroyRef, inject, OnInit, signal } from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { AuthService } from '../../core/services/essentials/auth.service';
import { fromEvent } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {Notification} from '../notification/notification';

@Component({
    selector: 'app-member-layout',
    standalone: true, // Mark as standalone if it doesn't need to be part of an Ng-Module
    imports: [CommonModule, RouterLink, RouterLinkActive, TitleCasePipe, Notification, RouterOutlet], // Added TitleCasePipe here
    templateUrl: './member-layout.html',
    styleUrl: './member-layout.css',
})
export class MemberLayout implements OnInit {
    private readonly destroyRef = inject(DestroyRef);
    protected readonly authService = inject(AuthService); // Make authService readonly

    // Signals for component state
    protected sidebarOpen = signal(false);
    // Initial screen size set during construction for immediate rendering and SSR compatibility
    protected screenSize = signal<'sm' | 'lg'>(
        typeof window !== 'undefined' && window.innerWidth >= 1024 ? 'lg' : 'sm'
    );

    // Computed signals for user data
    protected userName = computed(() => this.authService.currentUser()?.name || 'Guest');
    protected userRole = computed(() => {
        const roles = this.authService.currentUser()?.roles;
        // Join roles with ', ' for display, default to 'user' if no roles
        return roles?.length ? roles.join(', ') : 'user';
    });
    protected userInitials = computed(() => {
        const user = this.authService.currentUser();
        if (!user?.name) return '?';
        // Get initials from the user's name
        return user.name
            .split(' ')
            .map((n) => n[0])
            .join('')
            .toUpperCase();
    });

    // Computed signals for user roles (more direct naming)
    protected isAdmin = computed(() => this.authService.isAdmin());
    protected isReporter = computed(() => this.authService.isReporter());
    protected isConductor = computed(() => this.authService.isConductor());
    protected isSectionLeader = computed(() => this.authService.isSectionLeader());

    ngOnInit(): void {
        // Monitor window resize events with automatic cleanup
        if (typeof window !== 'undefined') {
            fromEvent(window, 'resize')
                .pipe(takeUntilDestroyed(this.destroyRef))
                .subscribe(() => {
                    this.screenSize.set(window.innerWidth >= 1024 ? 'lg' : 'sm');
                });
        }

        // Fetch user profile after a short delay, useful for initial data loading
        // Consider if this setTimeout is truly necessary or if data should be resolved via a guard/resolver
        setTimeout(() => this.fetchUserProfile(), 100); // Reduced delay
    }

    /**
     * Closes the mobile sidebar if the screen size is 'sm'.
     */
    protected closeMobileSidebar(): void {
        if (this.screenSize() === 'sm') {
            this.sidebarOpen.set(false);
        }
    }

    /**
     * Handles the user logout process.
     */
    protected logout(): void {
        this.authService.logout();
    }

    /**
     * Fetches the user profile and handles success/error cases.
     * On success, closes the mobile sidebar. On error, logs out the user.
     */
    private fetchUserProfile(): void {
        this.authService.fetchUserProfile().subscribe({
            next: () => {
                this.closeMobileSidebar();
            },
            error: (error) => {
                console.error('Failed to fetch user profile:', error); // Log the error for debugging
                this.authService.logout();
            },
        });
    }

    isMusician() {
        return this.isAdmin() || this.isReporter() || this.isConductor();
    }
}
