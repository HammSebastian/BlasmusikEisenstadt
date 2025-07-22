import {Component, inject, OnInit, signal} from '@angular/core';
import {DataService} from '../../../core/services/data.service';
import {EventModel} from '../../../core/models/event.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Loading} from '../../../shared/loading/loading';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {environment} from '../../../../environments/environment';
import {ErrorMessage} from '../../../shared/error-message/error-message';

@Component({
    selector: 'app-event-details',
    imports: [
        Loading,
        ErrorMessage
    ],
    templateUrl: './event-details.html',
    styleUrl: './event-details.css'
})
export class EventDetails implements OnInit {

    private readonly dataService = inject(DataService);
    private readonly router = inject(Router);
    private readonly route = inject(ActivatedRoute);
    private readonly sanitizer = inject(DomSanitizer);

    mapUrl!: SafeResourceUrl;

    //-------------------------------| Events |-------------------------------
    event = signal<EventModel | null>(null);
    eventSectionLoading = signal<boolean>(true);
    eventSectionError = signal<boolean>(false);
    eventSectionErrorMessage = signal<string>('');

    //-------------------------------| Events |-------------------------------

    ngOnInit() {
        const idParam = this.route.snapshot.paramMap.get('id');
        const id = idParam ? +idParam : null;

        if (id === null || isNaN(id)) {
            console.error('Invalid event ID');
            this.router.navigate(['/events']);
            return;
        }

        this.loadEventById(id);
    }

    private loadEventById(id: number) {
        this.dataService.loadEventById(id).subscribe({
            next: (response) => {
                this.event.set(response.data);
                this.eventSectionLoading.set(false);

                const address = `${response.data.location.street} ${response.data.location.number}, ${response.data.location.zipCode} ${response.data.location.city}`;
                this.setMapUrl(address);
            },
            error: (error) => {
                console.error('Error loading event:', error);
                this.router.navigate(['/events']);
                this.eventSectionError.set(true);
                this.eventSectionLoading.set(false);
                this.eventSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }

    setMapUrl(address: string) {
        const url = `https://www.google.com/maps/embed/v1/place?key=${environment.GOOGLE_MAPS_API_KEY}&q=${encodeURIComponent(address)}`;
        this.mapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
    }


    protected readonly encodeURIComponent = encodeURIComponent;
}
