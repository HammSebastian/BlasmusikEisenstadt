import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import {SeoService} from '../../../../core/services/seo';
import {NotificationService} from '../../../../core/services/notification';
import {Gig} from '../gigs-list/gigs-list';

@Component({
    selector: 'app-gig-detail',
    imports: [CommonModule, RouterLink],
    templateUrl: './gig-detail.html',
    styleUrl: './gig-detail.css'
})
export class GigDetail implements OnInit {
    private route = inject(ActivatedRoute);
    private router = inject(Router);
    private seoService = inject(SeoService);
    private notificationService = inject(NotificationService);

    gig = signal<Gig | null>(null);

    // Mock data - would typically come from a service
    private gigs: Gig[] = [
        {
            id: '1',
            title: 'Spring Concert 2025',
            date: '2025-03-28',
            time: '20:00',
            venue: 'Town Hall',
            address: '123 Main Street, City Center',
            description: 'Our annual spring concert featuring classical and contemporary pieces. Join us for an evening of beautiful brass music that showcases the talents of our dedicated musicians. This performance will include a mix of traditional brass band repertoire and modern arrangements that are sure to delight audiences of all ages.',
            status: 'upcoming',
            imageUrl: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=1200',
            attendees: 18,
            maxAttendees: 25
        },
        {
            id: '2',
            title: 'Easter Parade',
            date: '2025-04-12',
            time: '14:00',
            venue: 'Main Street',
            address: 'Main Street Parade Route',
            description: 'Traditional Easter parade through the city center. We\'ll be performing festive music while marching through the streets, bringing joy and celebration to the community during this special holiday season.',
            status: 'upcoming',
            imageUrl: 'https://images.pexels.com/photos/1047930/pexels-photo-1047930.jpeg?auto=compress&cs=tinysrgb&w=1200',
            attendees: 22,
            maxAttendees: 30
        }
    ];

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            const foundGig = this.gigs.find(g => g.id === id);
            this.gig.set(foundGig || null);

            if (foundGig) {
                this.seoService.updateMetaTags({
                    title: `${foundGig.title} - Blasmusik`,
                    description: foundGig.description,
                    keywords: `performance, ${foundGig.title}, ${foundGig.venue}, brass band`,
                    ogTitle: `${foundGig.title} - Blasmusik`,
                    ogDescription: foundGig.description,
                    ogImage: foundGig.imageUrl
                });
            }
        }
    }

    protected goBack(): void {
        this.router.navigate(['/gigs']);
    }

    protected getStatusClasses(status: string): string {
        switch (status) {
            case 'upcoming':
                return 'bg-primary-100 text-primary-800';
            case 'completed':
                return 'bg-accent-100 text-accent-800';
            case 'cancelled':
                return 'bg-red-100 text-red-800';
            default:
                return 'bg-secondary-100 text-secondary-800';
        }
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

    protected confirmAttendance(): void {
        this.notificationService.showSuccess('Attendance confirmed! We look forward to seeing you there.');
    }
}
