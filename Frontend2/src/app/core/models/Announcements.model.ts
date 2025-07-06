export interface AnnouncementsModel {
    id: number;
    title: string;
    message: string;
    type: 'info' | 'warning' | 'maintenance' | 'announcement';
    startDate: string;
    endDate?: string;
    createdBy: string;
}
