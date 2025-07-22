import {Component, HostListener, inject, OnInit, signal} from '@angular/core';
import {AuthService} from '@auth0/auth0-angular';
import {ThemeService} from '../../../core/theme/service/theme.service';
import {
    NavigationCancel,
    NavigationEnd,
    NavigationError,
    NavigationStart,
    Router,
    RouterLink,
    RouterOutlet
} from '@angular/router';
import {AsyncPipe, NgClass} from '@angular/common';
import {Footer} from '../footer/footer';
import {LoadingOverlay} from '../../loading-overlay/loading-overlay';
import {ThemeIcon} from '../../theme-icon/theme-icon';
import {Constants} from '../Constants';

@Component({
    selector: 'app-public-layout',
    imports: [
        RouterOutlet,
        AsyncPipe,
        NgClass,
        RouterLink,
        Footer,
        LoadingOverlay,
        ThemeIcon
    ],
    templateUrl: './public-layout.html',
    styleUrl: './public-layout.css'
})
export class PublicLayout implements OnInit {

    // Injects
    public readonly themeService = inject(ThemeService);
    private readonly authService = inject(AuthService); // Assuming you have an AuthService
    private readonly router = inject(Router);

    // Signals
    public isMenuOpen = signal(false);
    public isLoading = signal(false); // Example for a loading state
    public currentYear = new Date().getFullYear();

    // Async pipes for observables
    public isAuthenticated$ = this.authService.isAuthenticated$; // Assuming an observable from AuthService

    // Navigation Links
    public navLinks= Constants.navLinks;

    ngOnInit() {
        this.router.events.subscribe((event) => {
            // Use the enum members for comparison
            if (event instanceof NavigationStart) {
                this.isLoading.set(true);
            } else if (event instanceof NavigationEnd || event instanceof NavigationCancel || event instanceof NavigationError) {
                this.isLoading.set(false);
            }
        });
    }

    /**
     * Toggles the mobile menu open/close state.
     */
    public toggleMenu(): void {
        this.isMenuOpen.update((value) => !value);
    }

    /**
     * Handles user login action.
     */
    public login(): void {
        this.authService.loginWithRedirect();
    }

    /**
     * Handles user logout action.
     */
    public logout(): void {
        this.authService.logout(); // Call logout method from AuthService
        this.router.navigate(['/']); // Redirect to home after logout
    }

    /**
     * Navigates to the user dashboard.
     */
    public navigateToDashboard(): void {
        this.router.navigate(['/private/user/profile']);
    }

    /**
     * Closes the mobile menu when clicking outside of it.
     * @param event The click event.
     */
    @HostListener('document:click', ['$event'])
    onDocumentClick(event: MouseEvent): void {
        const target = event.target as HTMLElement;
        const navElement = document.querySelector('nav');
        const menuButton = document.querySelector('.md\\:hidden button'); // Target the mobile menu button

        if (this.isMenuOpen() && navElement && !navElement.contains(target) && menuButton && !menuButton.contains(target)) {
            this.isMenuOpen.set(false);
        }
    }
}
