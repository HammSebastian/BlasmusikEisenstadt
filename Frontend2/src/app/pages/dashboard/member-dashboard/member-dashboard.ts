import {Component, inject} from '@angular/core';
import {AuthService} from '../../../core/services/auth.service';
import {CommonModule} from '@angular/common';

@Component({
    selector: 'app-member-dashboard',
    imports: [CommonModule],
    templateUrl: './member-dashboard.html',
    styleUrl: './member-dashboard.css'
})
export class MemberDashboard {
    protected authService = inject(AuthService)
}
