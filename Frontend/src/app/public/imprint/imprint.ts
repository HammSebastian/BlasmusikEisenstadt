import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';

@Component({
  selector: 'app-imprint',
  imports: [],
  templateUrl: './imprint.html',
  styleUrl: './imprint.css'
})
export class Imprint implements OnInit {
    private readonly seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Impressum - Stadt- & Feuerwehrkapelle Eisenstadt',
            description: 'Finden Sie alle rechtlich notwendigen Informationen zur Stadt- & Feuerwehrkapelle Eisenstadt im Impressum.',
            keywords: 'impressum, stadtkapelle eisenstadt, kontakt, urheberrecht, haftungsausschluss, online streitbeilegung, dsgvo'
        });
    }
}
