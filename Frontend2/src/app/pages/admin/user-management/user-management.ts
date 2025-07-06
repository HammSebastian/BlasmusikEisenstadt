import {Component, inject, OnInit, signal} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {NgClass, TitleCasePipe} from "@angular/common";
import {SeoService} from '../../../core/services/seo.service';
import {NotificationService} from '../../../core/services/notification.service';
import {User} from '../../../core/models/generell/user.model';

@Component({
    selector: 'app-user-management',
    imports: [
        FormsModule,
        TitleCasePipe,
        NgClass
    ],
    templateUrl: './user-management.html',
    styleUrl: './user-management.css'
})
export class UserManagement implements OnInit {
    private notificationService = inject(NotificationService);

    searchTerm = '';
    roleFilter = '';
    statusFilter = '';

    private users = signal<User[]>([
        {
            id: '1',
            name: 'Sarah Johnson',
            email: 'sarah.johnson@email.com',
            role: 'admin',
            status: 'active',
            lastLogin: '2025-01-15T10:30:00Z',
            joinDate: '2020-01-15'
        },
        {
            id: '2',
            name: 'Michael Chen',
            email: 'michael.chen@email.com',
            role: 'conductor',
            status: 'active',
            lastLogin: '2025-01-14T18:45:00Z',
            joinDate: '2019-03-20'
        },
        {
            id: '3',
            name: 'Emily Rodriguez',
            email: 'emily.rodriguez@email.com',
            role: 'section-leader',
            status: 'active',
            lastLogin: '2025-01-13T14:20:00Z',
            joinDate: '2021-09-10'
        },
        {
            id: '4',
            name: 'David Thompson',
            email: 'david.thompson@email.com',
            role: 'musician',
            status: 'active',
            lastLogin: '2025-01-12T16:15:00Z',
            joinDate: '2018-06-05'
        },
        {
            id: '5',
            name: 'Lisa Martinez',
            email: 'lisa.martinez@email.com',
            role: 'musician',
            status: 'inactive',
            lastLogin: '2024-12-20T12:00:00Z',
            joinDate: '2022-02-14'
        }
    ]);

    filteredUsers = signal<User[]>([]);

    ngOnInit(): void {
        this.filteredUsers.set(this.users());
    }

    protected filterUsers(): void {
        let filtered = this.users();

        if (this.searchTerm.trim()) {
            const term = this.searchTerm.toLowerCase();
            filtered = filtered.filter(user =>
                user.name.toLowerCase().includes(term) ||
                user.email.toLowerCase().includes(term)
            );
        }

        if (this.roleFilter) {
            filtered = filtered.filter(user => user.role === this.roleFilter);
        }

        if (this.statusFilter) {
            filtered = filtered.filter(user => user.status === this.statusFilter);
        }

        this.filteredUsers.set(filtered);
    }

    protected updateUserRole(userId: string, event: Event): void {
        const target = event.target as HTMLSelectElement;
        const newRole = target.value as User['role'];

        this.users.update(users =>
            users.map(user =>
                user.id === userId ? {...user, role: newRole} : user
            )
        );

        this.filterUsers();
        this.notificationService.showSuccess(`User role updated to ${newRole}`);
    }

    protected resetPassword(userId: string): void {
        const user = this.users().find(u => u.id === userId);
        if (user) {
            // In a real app, this would send a password reset email
            this.notificationService.showSuccess(`Password reset email sent to ${user.email}`);
        }
    }

    protected toggleUserStatus(userId: string): void {
        this.users.update(users =>
            users.map(user =>
                user.id === userId
                    ? {...user, status: user.status === 'active' ? 'inactive' : 'active'}
                    : user
            )
        );

        this.filterUsers();
        this.notificationService.showSuccess('User status updated');
    }

    protected getStatusClasses(status: string): string {
        switch (status) {
            case 'active':
                return 'bg-accent-100 text-accent-800';
            case 'inactive':
                return 'bg-red-100 text-red-800';
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
