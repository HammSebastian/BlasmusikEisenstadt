export interface MemberModel {
    id: number;
    name: string;
    instrument: string;
    section: string;
    joinDate: string;
    avatarUrl: string;

    remarks?: { // Optional, da nicht jedes Mitglied Bemerkungen haben muss
        text: string;
        timestamp: string;
        type: 'accent' | 'primary' | 'yellow';
    }[]
}
