import { Routes } from '@angular/router';

export const membersRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./members-list/members-list').then(m => m.MembersList),
        title: 'Members - Blasmusik'
    },
    {
        path: ':id',
        loadComponent: () => import('./members-detail/members-detail').then(m => m.MembersDetail),
        title: 'Member Details - Blasmusik'
    }
];
