import { Component } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router'; // RouterModule for routerLink
import { CommonModule } from '@angular/common'; // For *ngIf, async pipe
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, RouterOutlet], // Ensure RouterModule is imported for routerLink
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'blasmusik-frontend';
  currentYear: number = new Date().getFullYear();

  constructor(public authService: AuthService, private router: Router) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
