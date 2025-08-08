import {ChangeDetectionStrategy, Component, computed, inject, OnInit, signal} from '@angular/core';
import {DataService} from '../../../core/services/data.service';
import {SeoService} from '../../../core/services/seo.service';
import {Loading} from '../../../shared/loading/loading';
import {EventModel} from '../../../core/models/event.model';
import {ErrorMessage} from '../../../shared/error-message/error-message';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [
        Loading,
        ErrorMessage
    ],
    templateUrl: './home.html',
    styleUrl: './home.css',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class Home implements OnInit {

    private readonly dataService = inject(DataService);
    private readonly seoService = inject(SeoService);

    //---------------- Welcome ----------------
    title = signal<string>('');
    subTitle = signal<string>('');
    buttonText = signal<string>('');
    backgroundImage = signal<string>('');
    homeSectionLoading = signal<boolean>(true);
    homeSectionError = signal<boolean>(false);
    homeSectionErrorMessage = signal<string>('');
    backgroundStyle = computed(() => `url('${this.backgroundImage()}')`);

    //---------------- About ----------------
    aboutImage = signal<string>('');
    aboutText = signal<string>('');
    aboutSectionLoading = signal<boolean>(true);
    aboutSectionError = signal<boolean>(false);
    aboutSectionErrorMessage = signal<string>('');

    //---------------- Events ----------------
    events = signal<EventModel[]>([]);
    eventSectionLoading = signal<boolean>(true);
    eventSectionError = signal<boolean>(false);
    eventSectionErrorMessage = signal<string>('');

    ngOnInit() {
        this.loadWelcomeData();
        this.loadAboutData();
        //this.loadEvents();
        this.setSeoMetadata();
    }

    /**
     * Sets SEO metadata for the home page
     */
    private setSeoMetadata(): void {
        // Set basic SEO metadata for the home page
        this.seoService.setSeoMetadata({
            metaTags: {
                title: 'Blasmusik Eisenstadt - Willkommen',
                description: 'Willkommen bei der Blasmusik Eisenstadt. Erfahren Sie mehr über unsere Konzerte, Veranstaltungen und Mitglieder.',
                keywords: 'Blasmusik, Eisenstadt, Musik, Konzerte, Veranstaltungen, Orchester',
                author: 'Blasmusik Eisenstadt',
                robots: 'index, follow',
                canonical: 'https://blasmusik-eisenstadt.at/'
            },
            openGraph: {
                title: 'Blasmusik Eisenstadt - Offizielle Website',
                description: 'Die offizielle Website der Blasmusik Eisenstadt mit Informationen zu Konzerten, Veranstaltungen und mehr.',
                type: 'website',
                url: 'https://blasmusik-eisenstadt.at/',
                image: 'https://blasmusik-eisenstadt.at/assets/images/Logo.png',
                siteName: 'Blasmusik Eisenstadt',
                locale: 'de_AT'
            },
            twitterCard: {
                card: 'summary_large_image',
                site: '@BlasmusikEisenstadt',
                title: 'Blasmusik Eisenstadt',
                description: 'Die offizielle Website der Blasmusik Eisenstadt',
                image: 'https://blasmusik-eisenstadt.at/assets/images/Logo.png',
                creator: '@BlasmusikEisenstadt'
            }
        });
    }

    loadWelcomeData() {
        this.homeSectionLoading.set(true);
        this.homeSectionError.set(false);
        this.homeSectionErrorMessage.set('');

        this.dataService.loadHomeWelcomeData().subscribe({
            next: (response) => {
                this.title.set(response.data.title);
                this.subTitle.set(response.data.subTitle);
                this.buttonText.set(response.data.buttonText);
                this.backgroundImage.set(response.data.backgroundImage);
                this.homeSectionLoading.set(false);
            },
            error: (error) => {
                console.error('Error loading welcome data:', error);
                this.homeSectionError.set(true);
                this.homeSectionLoading.set(false);
                this.homeSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
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

    loadEvents() {
        this.eventSectionLoading.set(true);
        this.eventSectionError.set(false);
        this.eventSectionErrorMessage.set('');

        this.dataService.loadEventsData().subscribe({
            next: (response) => {
                const shortened = response.data.map(event => ({
                    ...event,
                    description:
                        event.description.length > 50
                            ? event.description.slice(0, 50) + '...'
                            : event.description
                }));
                this.events.set(shortened);
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
