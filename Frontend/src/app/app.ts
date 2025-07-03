import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import {SeoService} from './core/services/seo';
import {AuthService} from './core/services/auth';
import {Layout} from './components/shared/layout/layout';

@Component({
  selector: 'app-root',
    imports: [CommonModule, RouterOutlet, Layout],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
    private seoService = inject(SeoService);
    private router = inject(Router);
    protected authService = inject(AuthService);

    ngOnInit(): void {
        // Set default meta tags
        this.seoService.updateMetaTags({
            title: 'Blasmusik - Brass Band Management',
            description: 'Professional brass band management system for performances, members, and events.',
            keywords: 'brass band, music, performances, management, events',
            ogImage: '/assets/images/og-image.jpg'
        });

        // Update meta tags on route changes
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                // Additional route-specific SEO handling can be added here
            });
    }
}
