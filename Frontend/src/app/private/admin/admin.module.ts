import {Routes} from '@angular/router';

export const adminRoutes: Routes = [
    {
        path: 'dashboard',
        loadComponent: () => import('./admin-dashboard/admin-dashboard').then(m => m.AdminDashboard),
        title: 'Admin Dashboard'
    },
    /*
    {
        path: 'users',
        loadComponent: () => import('./user-management/user-management').then(m => m.UserManagement),
        title: 'Benutzerverwaltung'
    },

     */
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
