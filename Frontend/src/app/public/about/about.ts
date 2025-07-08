import {Component, inject, OnInit, signal} from '@angular/core';
import {SeoService} from '../../core/services/essentials/seo.service';
import {AboutModel} from '../../core/models/public/about.model';
import {DataService} from '../../core/services/public/data.service';
import {CommonModule} from '@angular/common';

@Component({
    selector: 'app-about',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './about.html',
    styleUrl: './about.css'
})
export class About implements OnInit {
    private readonly seoService = inject(SeoService);
    private readonly dataService = inject(DataService);

    about = signal<AboutModel | null>(null);
    isLoading = signal<boolean>(true);
    error = signal<string | null>(null);

    ngOnInit(): void {
        this.seoService.updateMetaTags({
            title: 'Über den Verein',
            description: 'Finden Sie heraus, was der Verein in der Geschichte beigetragen hat und was unsere Mission ist.',
            keywords: 'blasmusik, eisenstadt, musikverein, feuerwehrkapelle, stadtkapelle, konzerte, burgenland, musik, termine'
        });

        this.loadAbout();
    }

    private loadAbout() {
        this.isLoading.set(true);
        this.error.set(null);

        console.log('Loading about data...');
        this.dataService.loadAbout().subscribe({
            next: (response) => {
                console.log('About API Response:', response);
                console.log('Response type:', typeof response);

                if (response) {
                    console.log('Response has result property:', 'result' in response);
                    const data = response?.result || response;
                    console.log('Processed data:', data);

                    this.about.set(data);
                } else {
                    console.warn('Empty or invalid response received');
                }

                this.isLoading.set(false);
                console.log()
            },
            error: (error) => {
                console.error('Fehler beim Laden der Über-uns-Daten:', {
                    error,
                    errorMessage: error?.message,
                    status: error?.status,
                    statusText: error?.statusText,
                    url: error?.url,
                    headers: error?.headers,
                    errorDetails: error?.error
                });
                this.error.set('Fehler beim Laden der Informationen. Bitte versuchen Sie es später erneut.');
                this.isLoading.set(false);
                this.setDefaultAboutData();
            }
        });
    }

    onImageError(event: Event): void {
        const imgElement = event.target as HTMLImageElement;
        imgElement.src = 'assets/images/HeroImage.png';
    }

    private setDefaultAboutData(): void {
        this.about.set({
            id: 1,
            imageUrl: 'assets/images/default-about.jpg',
            story: 'Die Stadt- & Feuerwehrkapelle Eisenstadt blickt auf eine mehr als 40-jährige Geschichte zurück. Wir sind stolz darauf, ein fester und aktiver Bestandteil der kulturellen Landschaft unserer Gemeinschaft zu sein.',
            missions: [
                {
                    id: 1,
                    title: 'Stadt- und Feuerwehrkapelle Eisenstadt',
                    description: 'Wir sind stolz darauf, ein fester und aktiver Bestandteil der kulturellen Landschaft unserer Gemeinschaft zu sein.',
                    imageUrl: 'assets/images/HeroImage.png',
                }
            ]
        });
    }

    protected readonly Array = Array;
}
