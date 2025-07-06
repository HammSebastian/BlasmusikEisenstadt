export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface NotificationModel {
    id: string;
    type: NotificationType;
    message: string;
    duration?: number;
}
