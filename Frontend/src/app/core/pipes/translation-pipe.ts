import {Pipe, PipeTransform} from '@angular/core';
import {NewsEnum} from '../models/enums/news.enum';

@Pipe({
    name: 'translation'
})
export class TranslationPipe implements PipeTransform {

    private readonly translationMap: Record<string, string> = {
        ANNOUNCEMENT: 'Ankündigung',
        REPORT: 'Bericht',
        BREAKING: 'Aktuelle Nachricht',
        DEADLINE: 'Frist',
        PROJECT_UPDATE: 'Projektupdate',
        INTERNAL: 'Intern',
        HIGHLIGHT: 'Hervorhebung',
        SPONSOR: 'Sponsor',
        OTHER: 'Andere'
    };

    transform(value: NewsEnum): string {
        return this.translationMap[value] ?? 'Unbekannt';
    }
}
