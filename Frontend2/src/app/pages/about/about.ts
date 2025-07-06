import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/seo.service';

@Component({
  selector: 'app-about',
  imports: [],
  templateUrl: './about.html',
  styleUrl: './about.css'
})
export class About implements OnInit {
    private seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'About Us - Blasmusik',
            description: 'Learn about Blasmusik brass band - our story, mission, and passion for music since 1985. Meet our leadership team and discover how to join us.',
            keywords: 'about blasmusik, brass band history, musical director, band members, join brass band',
            ogTitle: 'About Blasmusik - Our Story and Mission',
            ogDescription: 'Learn about our brass band\'s journey since 1985 and our commitment to musical excellence.',
            ogImage: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1200'
        });
    }
}
