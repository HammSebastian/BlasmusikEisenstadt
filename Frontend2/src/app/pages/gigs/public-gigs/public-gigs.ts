import {Component, inject, OnInit, signal} from '@angular/core';
import {SeoService} from '../../../core/services/seo.service';
import {PublicGig} from '../../../core/models/generell/PublicGig.model';
import {Router} from '@angular/router';

@Component({
    selector: 'app-public-gigs',
    imports: [],
    templateUrl: './public-gigs.html',
    styleUrl: './public-gigs.css'
})
export class PublicGigs implements OnInit {
    private seoService = inject(SeoService);

    upcomingGigs = signal<PublicGig[]>([
        {
            id: '1',
            title: 'Spring Concert 2025',
            date: '2025-03-28',
            time: '20:00',
            venue: 'Town Hall',
            address: '123 Main Street, City Center',
            description: 'Our annual spring concert featuring classical and contemporary pieces. Join us for an evening of beautiful brass music that showcases the talents of our dedicated musicians.',
            imageUrl: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=800',
            ticketInfo: 'Tickets: €15 adults, €8 students - Available at the door'
        },
        {
            id: '2',
            title: 'Easter Parade',
            date: '2025-04-12',
            time: '14:00',
            venue: 'Main Street',
            address: 'Main Street Parade Route',
            description: 'Traditional Easter parade through the city center. We\'ll be performing festive music while marching through the streets, bringing joy and celebration to the community.',
            imageUrl: 'https://images.pexels.com/photos/1047930/pexels-photo-1047930.jpeg?auto=compress&cs=tinysrgb&w=800',
            ticketInfo: 'Free event - No tickets required'
        },
        {
            id: '3',
            title: 'Charity Concert',
            date: '2025-05-20',
            time: '19:00',
            venue: 'Community Center',
            address: '456 Oak Avenue',
            description: 'Special charity concert to raise funds for local music education programs. All proceeds will go directly to supporting young musicians in our community.',
            imageUrl: 'https://images.pexels.com/photos/1114296/pexels-photo-1114296.jpeg?auto=compress&cs=tinysrgb&w=800',
            ticketInfo: 'Suggested donation: €20 - All proceeds go to charity'
        }
    ]);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Upcoming Performances - Blasmusik',
            description: 'Join us for our upcoming brass band performances and concerts. Check out our schedule of events and performances.',
            keywords: 'brass band performances, concerts, events, tickets, music shows',
            ogTitle: 'Upcoming Performances - Blasmusik',
            ogDescription: 'Join us for our upcoming brass band performances and concerts.',
            ogImage: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1200'
        });
    }

    constructor(private router: Router) {
    }

    protected formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    goBack() {
        this.router.navigate(['/']);
    }
}
