import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, TitleCasePipe} from "@angular/common";
import {SeoService} from '../../../core/services/seo.service';
import {NotificationService} from '../../../core/services/notification.service';
import {SiteMessagesAdminModel} from '../../../core/models/admin/site-messages-admin.model';

@Component({
  selector: 'app-site-messages',
    imports: [
        FormsModule,
        TitleCasePipe,
        NgClass
    ],
  templateUrl: './site-messages.html',
  styleUrl: './site-messages.css'
})
export class SiteMessages implements OnInit {
    private notificationService = inject(NotificationService);

    showCreateForm = signal(false);

    newMessage = {
        title: '',
        message: '',
        type: 'info' as SiteMessagesAdminModel['type'],
        isActive: true,
        startDate: '',
        endDate: ''
    };

    messages = signal<SiteMessagesAdminModel[]>([
        {
            id: '1',
            title: 'Maintenance Notice',
            message: 'The website will be undergoing scheduled maintenance on January 1st, 2026 from 2:00 AM to 4:00 AM. During this time, some features may be unavailable.',
            type: 'maintenance',
            isActive: true,
            startDate: '2025-12-31T23:00:00',
            endDate: '2026-01-01T06:00:00',
            createdBy: 'Sarah Johnson',
            createdAt: '2025-01-10T10:00:00Z'
        },
        {
            id: '2',
            title: 'Spring Concert Tickets Available',
            message: 'Tickets for our Spring Concert 2025 are now available! Get yours before they sell out.',
            type: 'announcement',
            isActive: true,
            startDate: '2025-01-15T00:00:00',
            endDate: '2025-03-28T20:00:00',
            createdBy: 'Michael Chen',
            createdAt: '2025-01-15T09:00:00Z'
        },
        {
            id: '3',
            title: 'Weather Alert',
            message: 'Due to severe weather conditions, tonight\'s rehearsal has been cancelled. We will resume next week.',
            type: 'warning',
            isActive: false,
            startDate: '2025-01-08T16:00:00',
            endDate: '2025-01-09T08:00:00',
            createdBy: 'Sarah Johnson',
            createdAt: '2025-01-08T15:30:00Z'
        }
    ]);

    ngOnInit(): void {
        // Set default start date to now
        const now = new Date();
        this.newMessage.startDate = now.toISOString().slice(0, 16);
    }

    protected createMessage(): void {
        const message: SiteMessagesAdminModel = {
            id: Date.now().toString(),
            ...this.newMessage,
            createdBy: 'Current Admin',
            createdAt: new Date().toISOString()
        };

        this.messages.update(messages => [message, ...messages]);
        this.notificationService.showSuccess('Site message created successfully');
        this.cancelCreate();
    }

    protected cancelCreate(): void {
        this.showCreateForm.set(false);
        this.newMessage = {
            title: '',
            message: '',
            type: 'info',
            isActive: true,
            startDate: new Date().toISOString().slice(0, 16),
            endDate: ''
        };
    }

    protected toggleMessage(messageId: string): void {
        this.messages.update(messages =>
            messages.map(message =>
                message.id === messageId
                    ? { ...message, isActive: !message.isActive }
                    : message
            )
        );
        this.notificationService.showSuccess('Message status updated');
    }

    protected editMessage(messageId: string): void {
        // In a real app, this would open an edit form
        this.notificationService.showInfo('Edit functionality would be implemented here');
    }

    protected deleteMessage(messageId: string): void {
        this.messages.update(messages =>
            messages.filter(message => message.id !== messageId)
        );
        this.notificationService.showSuccess('Message deleted');
    }

    protected getMessageBorderClass(type: string): string {
        switch (type) {
            case 'info':
                return 'border-primary-500';
            case 'warning':
                return 'border-yellow-500';
            case 'maintenance':
                return 'border-red-500';
            case 'announcement':
                return 'border-accent-500';
            default:
                return 'border-secondary-500';
        }
    }

    protected getMessageTypeClasses(type: string): string {
        switch (type) {
            case 'info':
                return 'bg-primary-100 text-primary-800';
            case 'warning':
                return 'bg-yellow-100 text-yellow-800';
            case 'maintenance':
                return 'bg-red-100 text-red-800';
            case 'announcement':
                return 'bg-accent-100 text-accent-800';
            default:
                return 'bg-secondary-100 text-secondary-800';
        }
    }

    protected formatDate(dateString: string): string {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }
}
