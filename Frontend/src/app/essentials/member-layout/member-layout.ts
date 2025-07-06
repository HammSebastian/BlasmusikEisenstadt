import {Component, computed, DestroyRef, inject, OnInit, signal} from '@angular/core';
import {Notification} from "../notification/notification";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {CommonModule, TitleCasePipe} from "@angular/common";
import {AuthService} from '../../core/services/essentials/auth.service';
import {fromEvent} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';

@Component({
    selector: 'app-member-layout',
    imports: [CommonModule, RouterLink, RouterLinkActive, Notification],
    templateUrl: './member-layout.html',
    styleUrl: './member-layout.css'
})
export class MemberLayout implements OnInit { // OnDestroy ist dank takeUntilDestroyed nicht mehr zwingend nötig
    // DestroyRef für das automatische Aufräumen von Subscriptions
    private readonly destroyRef = inject(DestroyRef);

    protected authService = inject(AuthService);

    // Signale für den Komponentenstatus
    sidebarOpen = signal(false);
    screenSize = signal<'sm' | 'lg'>('sm'); // Exakte Typisierung für Klarheit

    // Abgeleitete Signale für Benutzerdaten (computed())
    protected userName = computed(() => this.authService.currentUser()?.name || 'Guest');
    protected userRole = computed(() => this.authService.currentUser()?.role || 'user');
    protected isAdmin = computed(() => this.authService.isAdmin()); // Annahme: isAdmin() gibt ein boolean zurück

    protected userInitials = computed(() => {
        const user = this.authService.currentUser();
        if (!user?.name) return '?';
        return user.name.split(' ').map(n => n[0]).join('').toUpperCase();
    });

    constructor() {
        // Initialen Screen-Size setzen (wichtig für SSR oder sofortige Darstellung)
        if (typeof window !== 'undefined') {
            this.screenSize.set(window.innerWidth >= 1024 ? 'lg' : 'sm');
        }
    }

    ngOnInit(): void {
        // Überwachen von Fenstergrößenänderungen mit automatischem Aufräumen
        if (typeof window !== 'undefined') {
            fromEvent(window, 'resize')
                .pipe(takeUntilDestroyed(this.destroyRef)) // Stoppt die Subscription, wenn die Komponente zerstört wird
                .subscribe(() => {
                    this.screenSize.set(window.innerWidth >= 1024 ? 'lg' : 'sm');
                });
        }
    }

    protected closeMobileSidebar(): void {
        if (this.screenSize() === 'sm') {
            this.sidebarOpen.set(false);
        }
    }

    protected logout(): void {
        this.authService.logout();
    }
}
