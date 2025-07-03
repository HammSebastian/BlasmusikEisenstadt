import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthService} from '../../../core/services/auth';
import {SeoService} from '../../../core/services/seo';

@Component({
    selector: 'app-home',
    imports: [CommonModule],
    templateUrl: './home.html',
    styleUrl: './home.css'
})
export class Home implements OnInit {
    protected authService = inject(AuthService);
    private seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Dashboard - Blasmusik',
            description: 'Welcome to your brass band management dashboard. View announcements, upcoming events, and band statistics.',
            keywords: 'dashboard, brass band, announcements, events, statistics'
        });
    }
}
