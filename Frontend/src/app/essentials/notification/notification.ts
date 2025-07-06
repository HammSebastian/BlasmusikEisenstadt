import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NotificationService} from '../../core/services/essentials/notification.service';

@Component({
    selector: 'app-notification',
    imports: [CommonModule],
    templateUrl: './notification.html',
    styleUrl: './notification.css'
})
export class Notification {
    protected notificationService = inject(NotificationService);

    protected getNotificationClasses(type: string): string {
        const baseClasses = 'border-l-4';

        switch (type) {
            case 'success':
                return `${baseClasses} border-green-500`;
            case 'error':
                return `${baseClasses} border-red-500`;
            case 'warning':
                return `${baseClasses} border-yellow-500`;
            case 'info':
                return `${baseClasses} border-blue-500`;
            default:
                return baseClasses;
        }
    }

    protected getIconClasses(type: string): string {
        switch (type) {
            case 'success':
                return 'text-green-500';
            case 'error':
                return 'text-red-500';
            case 'warning':
                return 'text-yellow-500';
            case 'info':
                return 'text-blue-500';
            default:
                return 'text-secondary-500';
        }
    }

    protected remove(id: string): void {
        this.notificationService.remove(id);
    }
}
