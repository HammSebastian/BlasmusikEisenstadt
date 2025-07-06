export interface EventsModel {
    id: number;
    title: string;
    date: string;
    time: string;
    venue: string;
    address: string;
    description: string;
    imageUrl: string;
    ticketInfo?: string;
}
