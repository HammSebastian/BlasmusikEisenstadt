export interface GigModel {
    id: string;
    title: string;
    description: string;
    date: string;
    time: string;
    venue: string;
    address: string;
    imageUrl: string;
    note?: string;
    types: string[];
    attendance?: 'confirmed' | 'declined' | 'pending';
    dressCode?: string;
    additionalInfo?: string;
}
