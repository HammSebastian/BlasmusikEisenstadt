import { Routes } from '@angular/router';

// Definiere die Routen für das Reporter-Modul
export const reporterRoutes: Routes = [
    {
        path: 'dashboard', // Beispiel: /reporter/dashboard
        loadComponent: () => import('./reporter-dashboard/reporter-dashboard').then(m => m.ReporterDashboard),
        title: 'Reporter Dashboard'
    },
    {
        path: '', // Standardpfad für /reporter
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },
    {
        path: '**', // Wildcard für nicht übereinstimmende Pfade innerhalb von /reporter
        redirectTo: 'dashboard' // Leite zum Dashboard um
    }
];
