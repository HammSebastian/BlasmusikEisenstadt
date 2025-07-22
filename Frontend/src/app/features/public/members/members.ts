import {Component, computed, inject, OnInit, signal} from '@angular/core';
import {DataService} from '../../../core/services/data.service';
import {MembersModel} from '../../../core/models/members.model';
import {Loading} from '../../../shared/loading/loading';
import {ErrorMessage} from '../../../shared/error-message/error-message';
import {TranslationInstrumentPipe} from '../../../core/pipes/translation-instrument-pipe';
import {TranslationSectionPipe} from '../../../core/pipes/translation-section-pipe';
import {FormsModule} from '@angular/forms';
import {SectionEnum} from '../../../core/models/enums/section.enum';
import {InstrumentEnum} from '../../../core/models/enums/instrument.enum';

@Component({
  selector: 'app-members',
    imports: [
        Loading,
        ErrorMessage,
        TranslationInstrumentPipe,
        TranslationSectionPipe,
        FormsModule
    ],
  templateUrl: './members.html',
  styleUrl: './members.css'
})
export class Members implements OnInit {

    private readonly dataService = inject(DataService);

    //-------------------------------| Members |-------------------------------
    members = signal<MembersModel[]>([]);
    memberSectionLoading = signal<boolean>(true);
    memberSectionError = signal<boolean>(false);
    memberSectionErrorMessage = signal<string>('');
    filterSection = signal<SectionEnum | ''>('');
    filterInstrument = signal<InstrumentEnum | ''>('');
    sortOption = signal<string>('lastName');
    sectionEnums = signal<SectionEnum[]>(Object.values(SectionEnum).filter(v => typeof v === 'string') as unknown as SectionEnum[]);
    instrumentEnums = signal<InstrumentEnum[]>(Object.values(InstrumentEnum).filter(v => typeof v === 'string') as unknown as InstrumentEnum[]);
    //-------------------------------| Members |-------------------------------

    filteredMembers = computed(() => {
        let filtered = this.members();

        if (this.filterSection()) {
            filtered = filtered.filter(m => m.section === this.filterSection());
        }

        if (this.filterInstrument()) {
            filtered = filtered.filter(m => m.instrument === this.filterInstrument());
        }

        switch (this.sortOption()) {
            case 'firstName':
                filtered = filtered.slice().sort((a, b) => a.firstName.localeCompare(b.firstName));
                break;
            case 'oldest':
                filtered = filtered.slice().sort((a, b) => new Date(a.dateJoined).getTime() - new Date(b.dateJoined).getTime());
                break;
            case 'lastName':
            default:
                filtered = filtered.slice().sort((a, b) => a.lastName.localeCompare(b.lastName));
        }

        return filtered;
    });

    ngOnInit() {
        this.loadMembers();
    }

    loadMembers() {
        this.dataService.loadMembers().subscribe({
            next: (response) => {
                this.members.set(response.data);
                this.memberSectionLoading.set(false);
            },
            error: (error) => {
                console.error('Error loading events:', error);
                this.members.set([]);
                this.memberSectionError.set(true);
                this.memberSectionLoading.set(false);
                this.memberSectionErrorMessage.set(error.message ?? 'Unbekannter Fehler');
            }
        });
    }


    filteredAndSortedMembers = computed(() => {
        let filtered = this.members().filter(m =>
            (!this.filterSection() || m.section === this.filterSection()) &&
            (!this.filterInstrument() || m.instrument === this.filterInstrument())
        );

        if (this.sortOption() === 'firstName') {
            filtered.sort((a, b) => a.firstName.localeCompare(b.firstName));
        } else if (this.sortOption() === 'oldest') {
            filtered.sort((a, b) => new Date(a.dateJoined).getTime() - new Date(b.dateJoined).getTime());
        } else {
            filtered.sort((a, b) => a.lastName.localeCompare(b.lastName));
        }

        return filtered;
    });

    resetFilters() {
        this.filterSection.set('');
        this.filterInstrument.set('');
        this.sortOption.set('lastName');
    }
}
