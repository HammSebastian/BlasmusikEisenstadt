import { Routes } from '@angular/router';
import {roleGuard} from './core/authentication/guard/role-guard';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./shared/layout/public-layout/public-layout').then(m => m.PublicLayout),
        loadChildren: () => import('./features/public/public.routes').then(m => m.PublicRoutes),
        title: 'Home'
    },
    {
        path: 'private',
        loadComponent: () => import('./shared/layout/private-layout/private-layout').then(m => m.PrivateLayout),
        loadChildren: () => import('./features/private/private.routes').then(m => m.PrivateRoutes),
        canActivate: [roleGuard],
        title: 'Private'
    }
];
