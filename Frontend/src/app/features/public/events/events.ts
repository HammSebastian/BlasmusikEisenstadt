import {Component, inject, OnInit, signal} from '@angular/core';
import {EventModel} from '../../../core/models/event.model';
import {DataService} from '../../../core/services/data.service';
import {Loading} from '../../../shared/loading/loading';
import {ErrorMessage} from '../../../shared/error-message/error-message';

@Component({
  selector: 'app-events',
    imports: [
        Loading,
        ErrorMessage
    ],
  templateUrl: './events.html',
  styleUrl: './events.css'
})
export class Events implements OnInit {

    private readonly dataService = inject(DataService);

    //-------------------------------| Events |-------------------------------
    events = signal<EventModel[]>([]);
    eventSectionLoading = signal<boolean>(true);
    eventSectionError = signal<boolean>(false);
    eventSectionErrorMessage = signal<string>('');
    //-------------------------------| Events |-------------------------------

    ngOnInit() {
        this.loadEvents();
    }

    loadEvents() {
        this.dataService.loadEventsData().subscribe({
            next: (response) => {
                this.events.set(response.data);
                this.eventSectionLoading.set(false);
            },
            error: (error) => {
                console.error('Error loading events:', error);
                this.events.set([]);
                this.eventSectionError.set(true);
                this.eventSectionLoading.set(false);
                this.eventSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }
}
