import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    // Public routes
    {
        path: '',
        loadChildren: () => import('./public/public.module').then(m => m.publicModule)
    },

    // Admin routes - only accessible by admins
    {
        path: 'admin',
        canActivate: [roleGuard(['ADMIN'])],
        loadChildren: () => import('./private/admin/admin.module').then(m => m.adminRoutes),
        data: { title: 'Admin' }
    },

    // Reporter routes - only accessible by reporters and admins
    {
        path: 'reporter',
        canActivate: [roleGuard(['REPORTER', 'ADMIN'])],
        loadChildren: () => import('./private/reporter/reporter.module').then(m => m.reporterRoutes),
        data: { title: 'Reporter' }
    },

    // Conductor routes - only accessible by conductors and admins
    {
        path: 'conductor',
        canActivate: [roleGuard(['CONDUCTOR', 'ADMIN'])],
        loadChildren: () => import('./private/conductor/conductor.module').then(m => m.ConductorModule),
        data: { title: 'Conductor' }
    },

    // Section Leader routes - only accessible by section leaders and admins
    {
        path: 'section-leader',
        canActivate: [roleGuard(['SECTION_LEADER', 'ADMIN'])],
        loadChildren: () => import('./private/section_leader/section-leader.module').then(m => m.sectionLeaderRoutes),
        data: { title: 'Section Leader' }
    },

    // Musician routes - accessible by all authenticated users
    {
        path: 'musician',
        canActivate: [authGuard],
        loadChildren: () => import('./private/musician/musician.module').then(m => m.MusicianModule),
        data: { title: 'Musician' }
    },

    // Unauthorized route
    {
        path: 'unauthorized',
        loadComponent: () => import('./essentials/unauthorized/unauthorized').then(m => m.Unauthorized)
    },

    // Fallback route (must be last)
    {
        path: '**',
        loadComponent: () => import('./essentials/not-found/not-found').then(m => m.NotFound),
        pathMatch: 'full'
    }
];
