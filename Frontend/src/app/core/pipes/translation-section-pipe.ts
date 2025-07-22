import {Pipe, PipeTransform} from '@angular/core';
import {SectionEnum} from '../models/enums/section.enum';

@Pipe({
    name: 'translationSection'
})
export class TranslationSectionPipe implements PipeTransform {

    private readonly translationMap: Record<string, string> = {
        WOOD: 'Holz',
        HIGH_METAL: 'Hoches Blech',
        LOW_METAL: 'Tiefes Blech',
        DRUMS: 'Schlagwerk'
    };

    transform(value: SectionEnum): string {
        return this.translationMap[value] ?? 'Unbekannt';
    }
}
