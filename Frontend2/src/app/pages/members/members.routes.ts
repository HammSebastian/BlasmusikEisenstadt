import {Routes} from '@angular/router';

export const membersRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./member-list/member-list').then(m => m.MemberList),
        title: 'Members - Blasmusik'
    },
    {
        path: ':id',
        loadComponent: () => import('./member-detail/member-detail').then(m => m.MemberDetail),
        title: 'Member Details - Blasmusik'
    }
];
