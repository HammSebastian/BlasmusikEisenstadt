import {Component, inject, signal} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from '../../core/services/essentials/auth.service';
import {Notification} from '../notification/notification';

@Component({
    selector: 'app-layout',
    imports: [CommonModule, RouterLink, RouterLinkActive, Notification],
    templateUrl: './layout.html',
    styleUrl: './layout.css'
})
export class Layout {
    protected authService = inject(AuthService);
    mobileMenuOpen = signal(false);
}
