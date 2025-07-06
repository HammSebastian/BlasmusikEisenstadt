import {Routes} from '@angular/router';

export const gigsRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./member-gigs/member-gigs').then(m => m.MemberGigs),
        title: 'Performances - Blasmusik'
    },
    {
        path: ':id',
        loadComponent: () => import('./member-gigs-detail/member-gigs-detail').then(m => m.MemberGigsDetail),
        title: 'Performance Details - Blasmusik'
    }
];
