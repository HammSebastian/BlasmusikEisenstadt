export interface GigModel {
    id: number;
    title: string;
    description: string;
    date: string;
    time: string;
    venue: string;
    address: string;
    imageUrl: string;
    note?: string;
    gigType: 'brunch' | 'twilight' | 'concert' | 'performance' | 'other';
}
