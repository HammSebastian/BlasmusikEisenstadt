import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';
import {DataService} from '../../core/services/public/data.service';
import {GigModel} from '../../core/models/public/gig.model';
import {DatePipe} from '@angular/common';

@Component({
    selector: 'app-gigs',
    imports: [
        DatePipe
    ],
    templateUrl: './gigs.html',
    styleUrl: './gigs.css'
})
export class Gigs implements OnInit {
    private readonly seoService = inject(SeoService);
    private readonly dataService = inject(DataService);

    gigs: GigModel[] = [];

    ngOnInit() {
        this.loadGigs();
    }

    private loadGigs() {
        this.seoService.updateMetaTags({
            title: 'Auftritte & Konzerte',
            description: 'Überprüfen Sie die kommenden Auftritte und Konzerte der Stadt&Feuerwehrkapelle Eisenstadt',
            keywords: 'blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        this.dataService.loadGigs().subscribe(gigs => {
            this.gigs = gigs.result;
        })
    }

    goBack() {
        history.back();
    }
}
