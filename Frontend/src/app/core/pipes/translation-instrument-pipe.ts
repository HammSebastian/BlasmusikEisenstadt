import { Pipe, PipeTransform } from '@angular/core';
import {InstrumentEnum} from '../models/enums/instrument.enum';

@Pipe({
  name: 'translationInstrument'
})
export class TranslationInstrumentPipe implements PipeTransform {

    private readonly translationMap: Record<string, string> = {
        FLUTE: 'Querflöte',
        CLARINET: 'Klarinette',
        FLUGELHORN: 'Flügelhorn',
        TRUMPET: 'Trompete',
        HORN: 'Horn',
        TENORHORN: 'Tenorhorn',
        BARITONE: 'Bariton',
        POSAUNE: 'Posaune',
        TUBA: 'Tuba',
        DRUMS: 'Schlagwerk'
    }

    transform(value: InstrumentEnum): string {
        return this.translationMap[value] ?? 'Unbekannt';
    }
}
