import { Routes } from '@angular/router';

export const gigsRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./gigs-list/gigs-list').then(m => m.GigsList),
        title: 'Performances - Blasmusik'
    },
    {
        path: ':id',
        loadComponent: () => import('./gig-detail/gig-detail').then(m => m.GigDetail),
        title: 'Performance Details - Blasmusik'
    }
];
