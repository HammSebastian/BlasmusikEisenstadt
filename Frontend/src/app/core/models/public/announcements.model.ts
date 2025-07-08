export interface AnnouncementsModel {
    id: number;
    title: string;
    message: string;
    types: string[];
    startDate: string;
    endDate?: string;
    createdBy: string;
}
