import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/seo.service';

@Component({
  selector: 'app-imprint',
  imports: [],
  templateUrl: './imprint.html',
  styleUrl: './imprint.css'
})
export class Imprint implements OnInit {
    private seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Imprint - Blasmusik',
            description: 'Legal information and contact details for Blasmusik e.V. brass band organization.',
            keywords: 'imprint, legal information, contact, blasmusik organization'
        });
    }
}
