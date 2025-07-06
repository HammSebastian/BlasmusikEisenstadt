import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';

@Component({
  selector: 'app-privacy',
  imports: [],
  templateUrl: './privacy.html',
  styleUrl: './privacy.css'
})
export class Privacy implements OnInit {
    private readonly seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Datenschutzerklärung - Stadt- & Feuerwehrkapelle Eisenstadt',
            description: 'Erfahren Sie in unserer Datenschutzerklärung, wie die Stadt- & Feuerwehrkapelle Eisenstadt Ihre Daten schützt und verarbeitet.',
            keywords: 'datenschutz, dsgvo, privacy, stadtkapelle eisenstadt, datenverarbeitung, cookies, google maps, google fonts, server-log files, betroffenenrechte'
        });
    }
}
