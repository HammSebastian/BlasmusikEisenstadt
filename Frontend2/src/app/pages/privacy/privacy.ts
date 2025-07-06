import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/seo.service';

@Component({
  selector: 'app-privacy',
  imports: [],
  templateUrl: './privacy.html',
  styleUrl: './privacy.css'
})
export class Privacy implements OnInit {
    private seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Privacy Policy - Blasmusik',
            description: 'Privacy policy and data protection information for Blasmusik website and member portal.',
            keywords: 'privacy policy, data protection, GDPR, cookies, personal data'
        });
    }
}
