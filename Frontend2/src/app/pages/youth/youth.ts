import {Component, inject, OnInit} from '@angular/core';
import {SeoService} from '../../core/services/seo.service';

@Component({
  selector: 'app-youth',
  imports: [],
  templateUrl: './youth.html',
  styleUrl: './youth.css'
})
export class Youth implements OnInit {
    private seoService = inject(SeoService);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Our Youth Group - Blasmusik',
            description: 'Join our youth brass band program for ages 8-18. Individual lessons, ensemble playing, and performance opportunities. First month free trial.',
            keywords: 'youth brass band, music lessons, children music program, brass instruments, music education',
            ogTitle: 'Youth Brass Band Program - Blasmusik',
            ogDescription: 'Nurturing the next generation of brass band musicians with comprehensive music education.',
            ogImage: 'https://images.pexels.com/photos/1047930/pexels-photo-1047930.jpeg?auto=compress&cs=tinysrgb&w=1200'
        });
    }
}
