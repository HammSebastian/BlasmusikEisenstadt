import {InstrumentEnum} from './enums/instrument.enum';
import {SectionEnum} from './enums/section.enum';

export interface MembersModel {
    firstName: string;
    lastName: string;
    instrument: InstrumentEnum;
    avatarUrl: string;
    dateJoined: string;
    section: SectionEnum;

    //--------------------------------| Optional Fields |----------------------------------------
    notes?: string
    dateOfBirth?: string;
    phoneNumber?: string;
    number?: string;
    street?: string;
    zipCode?: string;
    city?: string;
    country?: string;
}
