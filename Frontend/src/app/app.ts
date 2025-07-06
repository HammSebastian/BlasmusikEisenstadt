import {Component, inject, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {CookieConsent} from './essentials/cookie-consent/cookie-consent';
import {AuthService} from './core/services/essentials/auth.service';
import {filter} from 'rxjs';
import {SeoService} from './core/services/essentials/seo.service';
import {Layout} from './essentials/layout/layout';
import {MemberLayout} from './essentials/member-layout/member-layout';

@Component({
    selector: 'app-root',
    imports: [RouterOutlet, CookieConsent, Layout, MemberLayout],
    templateUrl: './app.html',
    styleUrl: './app.css'
})
export class App implements OnInit {
    private readonly seoService = inject(SeoService);
    private readonly router = inject(Router);
    protected authService = inject(AuthService);

    ngOnInit(): void {
        // Set default meta tags
        this.seoService.updateMetaTags({
            title: 'Stadt- & Feuerwehrkapelle Eisenstadt',
            description: 'Offizielle Website der Stadt- & Feuerwehrkapelle Eisenstadt.',
            keywords: 'blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        // Update meta tags on route changes
        this.router.events
            .pipe(filter(event => event instanceof NavigationEnd))
            .subscribe(() => {
                // Additional route-specific SEO handling can be added here
            });
    }

    protected isPublicRoute(): boolean {
        return !this.router.url.startsWith('/member') && !this.router.url.startsWith('/admin');
    }
}
