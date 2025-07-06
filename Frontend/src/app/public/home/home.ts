import {Component, inject, OnInit} from '@angular/core';
import {DatePipe, NgStyle} from '@angular/common';
import {RouterLink} from '@angular/router';
import {SeoService} from '../../core/services/essentials/seo.service';
import {AuthService} from '../../core/services/essentials/auth.service';
import {HeroItemModel} from '../../core/models/public/heroItem.model';
import {AnnouncementsModel} from '../../core/models/public/announcements.model';
import {DataService} from '../../core/services/public/data.service';
import {GigModel} from '../../core/models/public/gig.model';
import {MemberModel} from '../../core/models/public/member.model';

@Component({
    selector: 'app-home',
    imports: [RouterLink, NgStyle, DatePipe],
    templateUrl: './home.html',
    styleUrl: './home.css'
})
export class Home implements OnInit {

    heroItems: HeroItemModel[] = [];
    announcements: AnnouncementsModel[] = [];
    gigs: GigModel[] = [];
    members: MemberModel[] = [];

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
    }

    constructor(private readonly dataService: DataService) {
    }

    private loadItems(): void {
        this.dataService.loadHeroItems().subscribe(items => {
            this.heroItems = items.result;
        });

        if (this.heroItems.length === 0) {
            this.heroItems = [
                {
                    id: 1,
                    title: 'Stadt- & Feuerwehrkapelle Eisenstadt',
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

    private loadGigs() {
        this.dataService.loadGigs().subscribe(gigs => {
            this.gigs = gigs.result.slice(0, 3);
        });
    }

    private loadMembers() {
        this.dataService.loadMembers().subscribe(members => {
            this.members = members.result;
        });
    }

    calculateYearsOfMusic() {
        return new Date().getFullYear() - 1874;
    }
}
