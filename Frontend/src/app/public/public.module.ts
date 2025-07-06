import {Routes} from '@angular/router';

export const publicModule: Routes = [
    {
        path: '',
        loadComponent: () => import('./home/home').then(m => m.Home),
        title: 'Stadt- & Feuerwehrkapelle Eisenstadt',
    },
    {
        path: 'gigs',
        loadComponent: () => import('./gigs/gigs').then(m => m.Gigs),
        title: 'Auftritte & Konzerte',
    },
    {
        path: 'members',
        loadComponent: () => import('./members/members').then(m => m.Members),
        title: 'Mitglieder',
    },
    {
        path: 'members/:id',
        loadComponent: () => import('./members-detail/members-detail').then(m => m.MembersDetail),
        title: 'Mitglieder',
    },
    {
        path: 'youth',
        loadComponent: () => import('./youth/youth').then(m => m.Youth),
        title: 'Die Feuerspritzen',
    },
    {
        path: 'imprint',
        loadComponent: () => import('./imprint/imprint').then(m => m.Imprint),
        title: 'Impressum',
    },
    {
        path: 'privacy',
        loadComponent: () => import('./privacy/privacy').then(m => m.Privacy),
        title: 'Datenschutzerklärung',
    },
    {
        path: 'about',
        loadComponent: () => import('./about/about').then(m => m.About),
        title: 'Über den Verein',
    },
    {
        path: 'auth/login',
        loadComponent: () => import('./auth/auth').then(m => m.Auth),
        title: 'Login',
    },
    {
        path: '**',
        loadComponent: () => import('./../essentials/not-found/not-found').then(m => m.NotFound),
        title: 'Seite nicht gefunden',
    }
]
