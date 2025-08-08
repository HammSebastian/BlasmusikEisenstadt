import {Routes} from '@angular/router';

export const PublicRoutes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadComponent: () => import('./home/home').then(m => m.Home),
        title: 'Home'
    },
    {
        path: 'events',
        loadComponent: () => import('./events/events').then(m => m.Events),
        title: 'Veranstaltungen'
    },
    {
        path: 'events/:id',
        loadComponent: () => import('./event-details/event-details').then(m => m.EventDetails),
        title: 'Veranstaltungdetails'
    },
    {
        path: 'news',
        loadComponent: () => import('./news/news').then(m => m.News),
        title: 'Neuigkeiten'
    },
    {
        path: 'gallery',
        loadComponent: () => import('./gallery/gallery').then(m => m.Gallery),
        title: 'Galerie'
    },
    {
        path: 'gallery/:slug',
        loadComponent: () => import('./gallery-card/gallery-card').then(m => m.GalleryCard),
        title: 'Galerie'
    },
    {
        path: 'history',
        loadComponent: () => import('./history/history').then(m => m.History),
        title: 'Geschichte'
    },
    {
        path: 'about',
        loadComponent: () => import('./about/about').then(m => m.About),
        title: 'About'
    },
    {
        path: 'members',
        loadComponent: () => import('./members/members').then(m => m.Members),
        title: 'Mitglieder'
    },
    {
        path: 'youth',
        loadComponent: () => import ('./youth/youth').then(m => m.Youth),
        title: 'Die Feuerspritzen'
    },
    {
        path: 'contact',
        loadComponent: () => import('./contact/contact').then(m => m.Contact),
        title: 'Kontakt'
    },
    {
        path: 'imprint',
        loadComponent: () => import('./imprint/imprint').then(m => m.Imprint),
        title: 'Impressum',
    },
    {
        path: 'privacy',
        loadComponent: () => import('./privacy/privacy').then(m => m.Privacy),
        title: 'Datenschutz',
    }
];
