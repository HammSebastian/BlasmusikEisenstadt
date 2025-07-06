import {Routes} from '@angular/router';

export const adminRoutes: Routes = [
    {
        path: '',
        loadComponent: () => import('./admin-dashboard/admin-dashboard').then(m => m.AdminDashboard),
        title: 'Admin Dashboard - Blasmusik'
    },
    {
        path: 'users',
        loadComponent: () => import('./user-management/user-management').then(m => m.UserManagement),
        title: 'User Management - Blasmusik'
    },
    {
        path: 'content',
        loadComponent: () => import('./content-management/content-management').then(m => m.ContentManagement),
        title: 'Content Management - Blasmusik'
    },
    {
        path: 'messages',
        loadComponent: () => import('./site-messages/site-messages').then(m => m.SiteMessages),
        title: 'Site Messages - Blasmusik'
    }
];
