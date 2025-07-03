import { Routes } from '@angular/router';
import {authGuard} from './core/guards/auth-guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadComponent: () => import('./components/features/home/home').then(m => m.Home),
        canActivate: [authGuard],
        title: 'Dashboard - Blasmusik'
    },
    {
        path: 'gigs',
        loadChildren: () => import('./components/features/gigs/gigs.routes').then(m => m.gigsRoutes),
        canActivate: [authGuard],
    },
    {
        path: 'members',
        loadChildren: () => import('./components/features/members/members.routes').then(m => m.membersRoutes),
        canActivate: [authGuard],
    },
    {
        path: 'auth',
        loadChildren: () => import('./components/features/auth/auth.routes').then(m => m.authRoutes),
    },
    {
        path: '**',
        loadComponent: () => import('./components/shared/not-found/not-found').then(m => m.NotFound),
        title: 'Page Not Found - Blasmusik'
    }
];
