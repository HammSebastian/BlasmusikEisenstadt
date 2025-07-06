import {Component, inject, OnInit} from '@angular/core';
import {RouterLink} from "@angular/router";
import {SeoService} from '../../../core/services/seo.service';
import {AuthService} from '../../../core/services/auth.service';
import {DataService} from '../../../core/services/data.service';
import {DatePipe, NgStyle} from '@angular/common';
import {HeroItemModelModel} from '../../../core/models/HeroItemModel.model';
import {AnnouncementsModel} from '../../../core/models/Announcements.model';

@Component({
    selector: 'app-public-dashboard',
    imports: [RouterLink, NgStyle, DatePipe],
    templateUrl: './public-dashboard.html',
    styleUrl: './public-dashboard.css'
})
export class PublicDashboard implements OnInit {

    heroItems: HeroItemModelModel[] = [];
    announcements: AnnouncementsModel[] = [];

    private seoService = inject(SeoService);
    protected authService = inject(AuthService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Blasmusik - Brass Band',
            description: 'Experience the power and beauty of brass band music with our passionate community of musicians. Founded in 1985, bringing joy through music.',
            keywords: 'brass band, music, concerts, performances, community, musicians',
            ogTitle: 'Blasmusik - Brass Band',
            ogDescription: 'Experience the power and beauty of brass band music with our passionate community of musicians.',
            ogImage: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1200'
        });

        this.loadItems();
        this.loadAnnouncements();
    }

    constructor(private readonly dataService: DataService) {
    }

    loadItems(): void {
        this.dataService.loadHeroItems().subscribe(items => {
            this.heroItems = items.result;
        });

        if (this.heroItems.length === 0) {
            this.heroItems = [
                {
                    id: 1,
                    title: 'Willkommen zu Blasmusik',
                    description: 'Willkommen auf unserer Webseite!',
                    imageUrl: 'https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fmedia04.meinbezirk.at%2Farticle%2F2023%2F12%2F05%2F2%2F37540742_XXL.jpg&f=1&nofb=1&ipt=b434e520a58800bcc08174262b99eaa72fde7aca595840b52a47e3317b16af99'
                }
            ];
        }
    }

    private loadAnnouncements() {
        this.dataService.loadAnnouncements().subscribe(announcements => {
            this.announcements = announcements.result;
        });
    }
}
