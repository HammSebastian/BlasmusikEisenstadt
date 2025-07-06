import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { NotificationComponent } from '../notification/notification.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, NotificationComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  protected authService = inject(AuthService);
  
  sidebarOpen = signal(false);
  screenSize = signal('sm');

  protected userInitials() {
    const user = this.authService.currentUser();
    if (!user?.name) return '?';
    return user.name.split(' ').map(n => n[0]).join('').toUpperCase();
  }

  protected closeMobileSidebar(): void {
    if (window.innerWidth < 1024) {
      this.sidebarOpen.set(false);
    }
  }

  protected logout(): void {
    this.authService.logout();
  }

  constructor() {
    // Handle window resize
    if (typeof window !== 'undefined') {
      window.addEventListener('resize', () => {
        this.screenSize.set(window.innerWidth >= 1024 ? 'lg' : 'sm');
      });
      
      // Set initial screen size
      this.screenSize.set(window.innerWidth >= 1024 ? 'lg' : 'sm');
    }
  }
}