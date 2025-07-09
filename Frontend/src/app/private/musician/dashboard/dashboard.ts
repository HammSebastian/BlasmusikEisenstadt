import {Component, inject, OnInit} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatCardModule} from '@angular/material/card';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatDialog} from '@angular/material/dialog';
import {DataService} from '../../../core/services/private/data.service';
import {GigModel} from '../../../core/models/private/gig.model';
import {Message} from '../../../core/models/private/message.model';
import {AnnouncementsModel} from '../../../core/models/public/announcements.model';
import {forkJoin} from 'rxjs';
import {AuthService} from '../../../core/services/essentials/auth.service';
import {MatChipRow} from '@angular/material/chips'; // Import forkJoin for parallel loading

@Component({
    selector: 'app-musician-dashboard',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatListModule,
        MatMenuModule,
        MatTooltipModule,
        MatSnackBarModule,
        MatChipRow
    ],
    templateUrl: './dashboard.html',
    styleUrls: ['./dashboard.css'],
    providers: [DatePipe]
})
export class Dashboard implements OnInit {
    // These will now be loaded from the backend or derived
    userName: string = 'Lade Daten...'; // Initial placeholder

    private readonly snackBar: MatSnackBar = inject(MatSnackBar);
    private readonly dataService: DataService = inject(DataService);
    private readonly authService: AuthService = inject(AuthService);

    upcomingGigs: GigModel[] = [];
    recentAnnouncements: AnnouncementsModel[] = [];

    ngOnInit(): void {
        this.loadGigs();
        this.loadAnnouncements();
        this.fetchUserProfile();
    }

    loadGigs(): void {
        this.dataService.loadGigs().subscribe(response => {
            this.upcomingGigs = response;
        });
    }

    loadAnnouncements(): void {
        this.dataService.loadAnnouncements().subscribe(response => {
            this.recentAnnouncements = response;
        });
    }

    private fetchUserProfile(): void {
        this.authService.fetchUserProfile().subscribe({
            next: () => {
                this.userName = this.authService.getCurrentUser()?.name || 'Guest';
            },
            error: (error) => {
                console.error('Failed to fetch user profile:', error); // Log the error for debugging
                this.authService.logout();
            },
        });
    }

    exportCalendar(): void {
        this.snackBar.open('Kalender wird exportiert...', 'OK', {duration: 2000});
        // Example: window.open('/api/calendar/export', '_blank');
    }

    // Helper methods for Announcement Types
    getAnnouncementTypeClass(type: string): string {
        switch (type) {
            case 'INFO': return 'type-info';
            case 'WARNING': return 'type-warning';
            case 'MAINTENANCE': return 'type-maintenance';
            case 'ANNOUNCEMENT': return 'type-announcement';
            default: return 'type-other';
        }
    }

    getAnnouncementTypeLabel(type: string): string {
        switch (type) {
            case 'INFO': return 'INFO';
            case 'WARNING': return 'WARNUNG';
            case 'MAINTENANCE': return 'WARTUNG';
            case 'ANNOUNCEMENT': return 'ANKÜNDIGUNG';
            default: return 'ALLGEMEIN';
        }
    }

    /*
    // Event related methods
    getAttendanceStatusColor(status?: string): string {
        switch (status) {
            case 'confirmed':
                return 'primary';
            case 'declined':
                return 'warn';
            default:
                return '';
        }
    }

    getAttendanceIcon(status?: string): string {
        switch (status) {
            case 'confirmed':
                return 'check_circle';
            case 'declined':
                return 'cancel';
            default:
                return 'radio_button_unchecked';
        }
    }

    getAttendanceTooltip(status?: string): string {
        switch (status) {
            case 'confirmed':
                return 'Zugesagt';
            case 'declined':
                return 'Abgesagt';
            default:
                return 'Noch nicht bestätigt';
        }
    }

    updateAttendance(eventId: string, status: 'confirmed' | 'declined'): void {
        const event = this.upcomingGigs.find(e => e.id === eventId);
        if (event) {
            event.attendance = status;
            // Call the data service to update attendance on the backend
            this.dataService.updateGigAttendance(eventId, status).subscribe({
                next: () => {
                    this.snackBar.open('Anwesenheit aktualisiert', 'OK', {duration: 3000});
                },
                error: (error) => {
                    console.error('Error updating attendance on backend:', error);
                    this.snackBar.open('Fehler beim Aktualisieren der Anwesenheit', 'OK', {duration: 3000});
                }
            });
        }
    }


    openScores(filter?: string): void {
        const queryParams = filter ? {filter} : undefined;
        this.router.navigate(['/scores'], {queryParams});
    }

    openEquipment(): void {
        this.router.navigate(['/equipment']);
    }

    openCarpool(): void {
        this.router.navigate(['/carpool']);
    }

    openProfile(): void {
        this.router.navigate(['/profile']);
    }

    openMessage(message: Message): void {
        // Mark as read locally and update unread count
        if (!message.read) {
            message.read = true;
            this.unreadMessagesCount = Math.max(0, this.unreadMessagesCount - 1);
            // In a real app, you'd also call a service to mark as read on the backend
            // this.dataService.markMessageAsRead(message.id).subscribe(...);
        }
        this.router.navigate(['/messages', message.id]);
    }

    viewAllEvents(): void {
        this.router.navigate(['/events']);
    }

    viewAllNews(): void {
        this.router.navigate(['/news']);
    }
    openAttendanceDialog(): void {
        this.snackBar.open('Anwesenheit melden', 'OK', {duration: 2000});
    }

    // Helper for formatting dates for display in template (optional, use if needed)
    formatDate(dateString: string): string {
        return this.datePipe.transform(dateString, 'dd.MM.yyyy') ?? '';
    }
 */
}
