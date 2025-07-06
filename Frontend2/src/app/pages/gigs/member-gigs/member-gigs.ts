import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, TitleCasePipe} from "@angular/common";
import {AuthService} from '../../../core/services/auth.service';
import {NotificationService} from '../../../core/services/notification.service';
import {GigMemberModel} from '../../../core/models/members/GigMember.model';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-member-gigs',
    imports: [
        FormsModule,
        TitleCasePipe,
        RouterLink,
        NgClass
    ],
  templateUrl: './member-gigs.html',
  styleUrl: './member-gigs.css'
})
export class MemberGigs implements OnInit {
    protected authService = inject(AuthService);
    private notificationService = inject(NotificationService);

    searchTerm = '';
    statusFilter = '';

    showAttendanceModal = signal(false);
    attendanceAction = signal<'confirm' | 'decline'>('confirm');
    attendanceReason = '';
    selectedGigId = '';

    private gigs = signal<GigMemberModel[]>([
        {
            id: '1',
            title: 'Spring Concert 2025',
            date: '2025-03-28',
            time: '20:00',
            venue: 'Town Hall',
            address: '123 Main Street, City Center',
            description: 'Our annual spring concert featuring classical and contemporary pieces. Join us for an evening of beautiful brass music.',
            status: 'upcoming',
            imageUrl: 'https://images.pexels.com/photos/1105666/pexels-photo-1105666.jpeg?auto=compress&cs=tinysrgb&w=800',
            attendees: 18,
            maxAttendees: 25,
            userAttendance: 'pending'
        },
        {
            id: '2',
            title: 'Easter Parade',
            date: '2025-04-12',
            time: '14:00',
            venue: 'Main Street',
            address: 'Main Street Parade Route',
            description: 'Traditional Easter parade through the city center. We\'ll be performing festive music while marching.',
            status: 'upcoming',
            imageUrl: 'https://images.pexels.com/photos/1047930/pexels-photo-1047930.jpeg?auto=compress&cs=tinysrgb&w=800',
            attendees: 22,
            maxAttendees: 30,
            userAttendance: 'confirmed',
            userAttendanceReason: 'Looking forward to it!'
        },
        {
            id: '3',
            title: 'Christmas Market Performance',
            date: '2024-12-15',
            time: '18:30',
            venue: 'Christmas Market',
            address: 'Town Square',
            description: 'Festive Christmas performance at the annual Christmas market. Playing traditional carols and holiday favorites.',
            status: 'completed',
            imageUrl: 'https://images.pexels.com/photos/1190297/pexels-photo-1190297.jpeg?auto=compress&cs=tinysrgb&w=800',
            attendees: 20,
            maxAttendees: 25,
            userAttendance: 'confirmed'
        },
        {
            id: '4',
            title: 'Charity Concert',
            date: '2025-05-20',
            time: '19:00',
            venue: 'Community Center',
            address: '456 Oak Avenue',
            description: 'Special charity concert to raise funds for local music education programs.',
            status: 'upcoming',
            imageUrl: 'https://images.pexels.com/photos/1114296/pexels-photo-1114296.jpeg?auto=compress&cs=tinysrgb&w=800',
            attendees: 15,
            maxAttendees: 25,
            userAttendance: 'declined',
            userAttendanceReason: 'Family commitment that evening'
        }
    ]);

    filteredGigs = signal<GigMemberModel[]>([]);

    ngOnInit(): void {
        this.filteredGigs.set(this.gigs());
    }

    protected filterGigs(): void {
        let filtered = this.gigs();

        // Filter by search term
        if (this.searchTerm.trim()) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(gig =>
                gig.title.toLowerCase().includes(term) ||
                gig.venue.toLowerCase().includes(term) ||
                gig.description.toLowerCase().includes(term)
            );
        }

        // Filter by status
        if (this.statusFilter) {
            filtered = filtered.filter(gig => gig.status === this.statusFilter);
        }

        this.filteredGigs.set(filtered);
    }

    protected confirmAttendance(gigId: string): void {
        this.selectedGigId = gigId;
        this.attendanceAction.set('confirm');
        this.attendanceReason = '';
        this.showAttendanceModal.set(true);
    }

    protected declineAttendance(gigId: string): void {
        this.selectedGigId = gigId;
        this.attendanceAction.set('decline');
        this.attendanceReason = '';
        this.showAttendanceModal.set(true);
    }

    protected submitAttendance(): void {
        if (this.attendanceAction() === 'decline' && !this.attendanceReason.trim()) {
            return;
        }

        this.gigs.update(gigs =>
            gigs.map(gig =>
                gig.id === this.selectedGigId
                    ? {
                        ...gig,
                        userAttendance: this.attendanceAction() === 'confirm' ? 'confirmed' : 'declined',
                        userAttendanceReason: this.attendanceReason.trim() || undefined,
                        attendees: this.attendanceAction() === 'confirm'
                            ? gig.attendees + (gig.userAttendance === 'pending' ? 1 : 0)
                            : gig.attendees - (gig.userAttendance === 'confirmed' ? 1 : 0)
                    }
                    : gig
            )
        );

        this.filterGigs();
        this.closeAttendanceModal();

        const action = this.attendanceAction() === 'confirm' ? 'confirmed' : 'declined';
        this.notificationService.showSuccess(`Attendance ${action} successfully`);
    }

    protected closeAttendanceModal(): void {
        this.showAttendanceModal.set(false);
        this.attendanceReason = '';
        this.selectedGigId = '';
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

    protected getAttendanceClasses(attendance: string): string {
        switch (attendance) {
            case 'confirmed':
                return 'bg-accent-100 text-accent-800';
            case 'declined':
                return 'bg-red-100 text-red-800';
            case 'pending':
                return 'bg-yellow-100 text-yellow-800';
            default:
                return 'bg-secondary-100 text-secondary-800';
        }
    }

    protected formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            weekday: 'short',
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    }
}
