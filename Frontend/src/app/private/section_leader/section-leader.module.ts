import {Routes} from '@angular/router';

export const sectionLeaderRoutes: Routes = [
    {
        path: 'dashboard',
        loadComponent: () => import('./section-leader/section-leader').then(m => m.SectionLeader)
    },
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },
    {
        path: '**',
        loadComponent: () => import('./../../essentials/not-found/not-found').then(m => m.NotFound),
        pathMatch: 'full'
    }
];
