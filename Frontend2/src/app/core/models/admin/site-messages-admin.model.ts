export interface SiteMessagesAdminModel {
    id: string;
    title: string;
    message: string;
    type: 'info' | 'warning' | 'maintenance' | 'announcement';
    isActive: boolean;
    startDate: string;
    endDate?: string;
    createdBy: string;
    createdAt: string;
}
