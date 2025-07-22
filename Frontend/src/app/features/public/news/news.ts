import {Component, inject, OnInit, signal} from '@angular/core';
import {DataService} from '../../../core/services/data.service';
import {NewsModel} from '../../../core/models/news.model';
import {Loading} from '../../../shared/loading/loading';
import {NewsEnum} from '../../../core/models/enums/news.enum';
import {TranslationPipe} from '../../../core/pipes/translation-pipe';
import {ErrorMessage} from '../../../shared/error-message/error-message';

@Component({
  selector: 'app-news',
    imports: [
        Loading,
        TranslationPipe,
        ErrorMessage
    ],
  templateUrl: './news.html',
  styleUrl: './news.css'
})
export class News implements OnInit {

    private readonly dataService = inject(DataService);

    //-------------------------------| Events |-------------------------------
    news = signal<NewsModel[]>([]);
    newsSectionLoading = signal<boolean>(true);
    newsSectionError = signal<boolean>(false);
    newsSectionErrorMessage = signal<string>('');
    //-------------------------------| Events |-------------------------------

    ngOnInit() {
        this.loadEvents();
    }

    loadEvents() {
        this.dataService.loadNewsData().subscribe({
            next: (response) => {
                console.log(response.data);
                const newsTypeGermanMap = {
                    [NewsEnum.ANNOUNCEMENT]: 'Ankündigung',
                    [NewsEnum.REPORT]: 'Bericht',
                    [NewsEnum.BREAKING]: 'Eilmeldung',
                    [NewsEnum.DEADLINE]: 'Frist',
                    [NewsEnum.PROJECT_UPDATE]: 'Projekt-Update',
                    [NewsEnum.INTERNAL]: 'Intern',
                    [NewsEnum.HIGHLIGHT]: 'Highlight',
                    [NewsEnum.SPONSOR]: 'Sponsor',
                    [NewsEnum.OTHER]: 'Sonstiges',
                };
                this.news.set(response.data.map(item => ({
                    ...item,
                    newsTypeGerman: newsTypeGermanMap[item.newsType]
                })));
                this.newsSectionLoading.set(false);
            },

            error: (error) => {
                console.error('Error loading events:', error);
                this.news.set([]);
                this.newsSectionError.set(true);
                this.newsSectionLoading.set(false);
                this.newsSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }
}
