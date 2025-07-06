import {Routes} from '@angular/router';
import {authGuard} from './core/guards/auth.guard';
import {adminGuard} from './core/guards/admin.guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
    },
    // Public routes (accessible without login)
    {
        path: '',
        loadChildren: () => import('./pages/publicRoutes/public.routes').then(m => m.publicRoutes),
    },
    // Protected routes (require login)
    {
        path: 'member-dashboard',
        loadComponent: () => import('./pages/dashboard/member-dashboard/member-dashboard').then(m => m.MemberDashboard),
        canActivate: [authGuard],
        title: 'Dashboard - Blasmusik'
    },
    {
        path: 'member-gigs',
        loadChildren: () => import('./pages/gigs/gigs.routes').then(m => m.gigsRoutes),
        canActivate: [authGuard],
    },
    {
        path: 'members',
        loadChildren: () => import('./pages/members/members.routes').then(m => m.membersRoutes),
        canActivate: [authGuard],
    },
    {
        path: 'admin',
        loadChildren: () => import('./pages/admin/admin.routes').then(m => m.adminRoutes),
        canActivate: [authGuard, adminGuard],
    },
    {
        path: 'auth',
        loadChildren: () => import('./pages/authentication/auth.routes').then(m => m.authRoutes),
    },
    {
        path: '**',
        loadComponent: () => import('./shared/components/not-found/not-found.component').then(m => m.NotFoundComponent),
        title: 'Page Not Found - Blasmusik'
    }
];
