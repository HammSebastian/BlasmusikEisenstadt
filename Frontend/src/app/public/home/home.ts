import {Component, inject, OnInit} from '@angular/core';
import {DatePipe, NgClass, NgStyle} from '@angular/common';
import {RouterLink} from '@angular/router';
import {SeoService} from '../../core/services/essentials/seo.service';
import {AuthService} from '../../core/services/essentials/auth.service';
import {HeroItemModel} from '../../core/models/public/heroItem.model';
import {AnnouncementsModel} from '../../core/models/public/announcements.model';
import {DataService} from '../../core/services/public/data.service';
import {GigModel} from '../../core/models/public/gig.model';
import {MemberModel} from '../../core/models/public/member.model';
import {AboutModel} from '../../core/models/public/about.model';

@Component({
    selector: 'app-home',
    imports: [RouterLink, NgStyle, DatePipe, NgClass],
    templateUrl: './home.html',
    styleUrl: './home.css'
})
export class Home implements OnInit {

    heroItems: HeroItemModel = {
        id: 1,
        title: 'Stadt- & Feuerwehrkapelle Eisenstadt',
        description: 'Willkommen auf unserer Webseite!',
        imageUrl: 'https://images.pexels.com/photos/1065084/pexels-photo-1065084.jpeg?auto=compress&cs=tinysrgb&w=150'
    }
    announcements: AnnouncementsModel[] = [];
    gigs: GigModel[] = [];
    members: MemberModel[] = [];
    about: AboutModel = {
        id: 1,
        imageUrl: 'https://images.pexels.com/photos/1065084/pexels-photo-1065084.jpeg?auto=compress&cs=tinysrgb&w=150',
        story: '',
        missions: [],
    };

    private readonly seoService = inject(SeoService);
    protected readonly authService = inject(AuthService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Stadt- & Feuerwehrkapelle Eisenstadt',
            description: 'Offizielle Website der Stadt- & Feuerwehrkapelle Eisenstadt',
            keywords: 'blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        this.loadItems();
        this.loadAnnouncements();
        this.loadGigs();
        this.loadMembers();
        this.loadAbout();
    }

    constructor(private readonly dataService: DataService) {
    }

    private loadItems(): void {
        this.dataService.loadHeroItems().subscribe(items => {
            this.heroItems = items;
        });
    }

    private loadAnnouncements() {
        this.dataService.loadAnnouncements().subscribe(rawAnnouncements => {
            this.announcements = rawAnnouncements.map(a => ({
                id: a.id,
                title: a.title,
                message: a.message,
                types: a.types,
                startDate: a.startDate,
                endDate: a.endDate,
                createdBy: a.createdBy
            }));
        });
    }

    private loadGigs() {
        this.dataService.loadGigs().subscribe({
            next: (response) => {
                // Handle both direct array response and response with result property
                const gigsArray = Array.isArray(response) ? response : (response?.result || []);
                this.gigs = gigsArray.slice(0, 3);
            },
            error: (error) => {
                console.error('Error loading gigs:', error);
                this.gigs = [];
            }
        });
    }

    private loadMembers() {
        this.dataService.loadMembers().subscribe({
            next: (response) => {
                // Handle both direct array response and response with result property
                this.members = Array.isArray(response) ? response : (response?.result || []);
            },
            error: (error) => {
                console.error('Error loading members:', error);
                this.members = [];
            }
        });
    }

    private loadAbout() {
        this.dataService.loadAbout().subscribe({
            next: (response) => {
                // Handle both direct object response and response with result property
                this.about = response?.result || response || this.about; // Fallback to current about if response is invalid
            },
            error: (error) => {
                console.error('Error loading about:', error);
                // Keep the default about data if there's an error
            }
        });
    }

    calculateYearsOfMusic() {
        return new Date().getFullYear() - 1874;
    }

    aboutShort() {
        if (!this.about.story) {
            return 'Lade die Geschichte...';
        }
        return this.about.story
            .split('\n')
            .slice(0, 3)
            .join('\n');
    }

}
