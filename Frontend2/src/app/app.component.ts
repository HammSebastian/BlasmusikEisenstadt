import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterOutlet, NavigationEnd} from '@angular/router';
import {CommonModule} from '@angular/common';
import {filter} from 'rxjs/operators';

import {LayoutComponent} from './shared/components/layout/layout.component';
import {PublicLayoutComponent} from './shared/components/public-layout/public-layout.component';
import {CookieConsentComponent} from './shared/components/cookie-consent/cookie-consent.component';
import {SeoService} from './core/services/seo.service';
import {AuthService} from './core/services/auth.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, RouterOutlet, LayoutComponent, PublicLayoutComponent, CookieConsentComponent],
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    private seoService = inject(SeoService);
    private router = inject(Router);
    protected authService = inject(AuthService);

    ngOnInit(): void {
        // Set default meta tags
        this.seoService.updateMetaTags({
            title: 'Blasmusik - Brass Band Management',
            description: 'Professional brass band management system for performances, members, and events.',
            keywords: 'brass band, music, performances, management, events',
            ogImage: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1200'
        });

        // Update meta tags on route changes
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                // Additional route-specific SEO handling can be added here
            });
    }

    protected isPublicRoute(): boolean {
        return this.router.url.startsWith('/public');
    }
}
