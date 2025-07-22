import {Component, OnInit, signal, inject} from '@angular/core';
import {ErrorMessage} from "../../../shared/error-message/error-message";
import {Loading} from "../../../shared/loading/loading";
import {DataService} from '../../../core/services/data.service';

@Component({
  selector: 'app-about',
    imports: [
        ErrorMessage,
        Loading
    ],
  templateUrl: './about.html',
  styleUrl: './about.css'
})
export class About implements OnInit {

    private readonly dataService = inject(DataService);

    //---------------- About ----------------
    aboutImage = signal<string>('');
    aboutText = signal<string>('');
    aboutSectionLoading = signal<boolean>(true);
    aboutSectionError = signal<boolean>(false);
    aboutSectionErrorMessage = signal<string>('');
    //---------------- About ----------------

    ngOnInit() {
        this.loadAboutData();
    }

    loadAboutData() {
        this.aboutSectionLoading.set(true);
        this.aboutSectionError.set(false);
        this.aboutSectionErrorMessage.set('');

        this.dataService.loadAboutData().subscribe({
            next: (response) => {
                this.aboutText.set(response.data.aboutText);
                this.aboutImage.set(response.data.aboutImage);
                this.aboutSectionLoading.set(false);
            },
            error: (error) => {
                console.error('Error loading about data:', error);
                this.aboutSectionError.set(true);
                this.aboutSectionLoading.set(false);
                this.aboutSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }

}
