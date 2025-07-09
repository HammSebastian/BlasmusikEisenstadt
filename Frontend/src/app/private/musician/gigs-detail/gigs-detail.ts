import {Component, inject, OnInit} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatCardModule} from '@angular/material/card';
import {MatDividerModule} from '@angular/material/divider';
import {MatChipsModule} from '@angular/material/chips';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {DataService} from '../../../core/services/private/data.service';
import {GigModel} from '../../../core/models/private/gig.model';
import {switchMap, throwError, Observable} from 'rxjs'; // Keep Observable and throwError
import {MatDialog} from '@angular/material/dialog';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
// import {ApiResponse} from '../../../core/models/essentials/apiResponse.model'; // You might not need this import anymore if getGigById is the only one using it, but keep it if other methods use it.

@Component({
    selector: 'app-gigs-detail',
    standalone: true,
    imports: [
        CommonModule,
        RouterModule,
        MatButtonModule,
        MatIconModule,
        MatCardModule,
        MatDividerModule,
        MatChipsModule,
        MatTooltipModule,
        MatSnackBarModule,
        MatProgressSpinner
    ],
    templateUrl: './gigs-detail.html',
    styleUrls: ['./gigs-detail.css'],
    providers: [DatePipe]
})
export class GigsDetail implements OnInit {
    gig: GigModel = {
        id: '',
        title: '',
        description: '',
        date: '',
        time: '',
        venue: '',
        address: '',
        imageUrl: '',
        additionalInfo: '',
        types: [],
        attendance: 'pending',
        dressCode: ''
    };
    isLoading = true;
    attendanceStatus: 'confirmed' | 'declined' | 'pending' = 'pending';

    private readonly router: Router = inject(Router);
    private readonly route: ActivatedRoute = inject(ActivatedRoute);
    private readonly dialog: MatDialog = inject(MatDialog);
    private readonly snackBar: MatSnackBar = inject(MatSnackBar);
    private readonly datePipe: DatePipe = inject(DatePipe);
    private readonly dataService: DataService = inject(DataService);


    ngOnInit(): void {
        this.loadGig();
    }

    loadGig(): void {
        this.isLoading = true;
        this.route.paramMap.pipe(
            switchMap(params => {
                const id = params.get('id');
                if (!id) {
                    this.snackBar.open('Keine Auftritts-ID angegeben.', 'OK', {duration: 5000});
                    this.router.navigate(['/dashboard']);
                    // FIX: Return Observable<GigModel> for consistency when no ID
                    return throwError(() => new Error('Gig ID not provided')) as Observable<GigModel>;
                }
                return this.dataService.getGigById(id); // Now returns Observable<GigModel>
            })
        ).subscribe({
            next: (response) => {
                this.gig = response;
                this.isLoading = false;
            },
            error: (error) => {
                console.error('Error loading gig:', error);
                this.isLoading = false;
                this.snackBar.open('Fehler beim Laden des Auftritts: ' + (error.message || 'Unbekannter Fehler'), 'OK', {duration: 5000});
                this.router.navigate(['/dashboard']);
            }
        });
    }

    updateAttendance(status: 'confirmed' | 'declined'): void {
        if (!this.gig || !this.gig.id) {
            console.warn('Attempted to update attendance without a valid gig object or ID.');
            return;
        }

        // Note: dataService.updateGigAttendance might still return ApiResponse<GigModel>,
        // so its 'next' handler might need to access 'response.result'.
        // If it directly returns GigModel, then 'next: (response) => { ... }' would work as is.
        this.dataService.updateGigAttendance(this.gig.id, status).subscribe({
            next: (responseFromUpdate) => { // Renamed param to avoid confusion
                // If updateGigAttendance still returns ApiResponse, use responseFromUpdate.result
                // this.attendanceStatus = responseFromUpdate.result.attendance || status;
                // Otherwise, use:
                this.attendanceStatus = status; // Assuming success means status is updated
                this.snackBar.open('Anwesenheit aktualisiert', 'OK', {duration: 3000});
            },
            error: (error) => {
                console.error('Error updating attendance:', error);
                this.snackBar.open('Fehler beim Aktualisieren der Anwesenheit', 'OK', {duration: 3000});
            }
        });
    }

    getAttendanceStatusColor(status: string): string {
        switch (status) {
            case 'confirmed':
                return 'primary';
            case 'declined':
                return 'warn';
            default:
                return '';
        }
    }

    getAttendanceIcon(status: string): string {
        switch (status) {
            case 'confirmed':
                return 'check_circle';
            case 'declined':
                return 'cancel';
            default:
                return 'radio_button_unchecked';
        }
    }

    getAttendanceTooltip(status: string): string {
        switch (status) {
            case 'confirmed':
                return 'Zugesagt';
            case 'declined':
                return 'Abgesagt';
            default:
                return 'Noch nicht bestätigt';
        }
    }

    formatDate(dateString: string): string {
        return this.datePipe.transform(dateString, 'EEEE, dd.MM.yyyy') ?? '';
    }

    openMaps(address: string): void {
        const mapsUrl = `http://googleusercontent.com/maps.google.com/?q=${encodeURIComponent(address)}`; // Corrected Google Maps URL
        window.open(mapsUrl, '_blank');
    }

    copyToClipboard(text: string): void {
        navigator.clipboard.writeText(text).then(() => {
            this.snackBar.open('In die Zwischenablage kopiert', 'OK', {duration: 2000});
        });
    }

    goBack() {
        window.history.back();
    }
}
