export interface SiteMessagesModel {
    id: string;
    title: string;
    message: string;
    type: 'info' | 'warning' | 'maintenance' | 'announcement';
    startDate: string;
    endDate?: string;
    createdBy: string;
}
