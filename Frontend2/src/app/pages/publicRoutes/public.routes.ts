import { Routes } from '@angular/router';

export const publicRoutes: Routes = [
    {
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
    },
    {
        path: 'home',
        loadComponent: () => import('./../dashboard/public-dashboard/public-dashboard').then(m => m.PublicDashboard),
        title: 'Blasmusik - Brass Band'
    },
    {
        path: 'gigs',
        loadComponent: () => import('./../gigs/public-gigs/public-gigs').then(m => m.PublicGigs),
        title: 'Upcoming Performances - Blasmusik'
    },
    {
        path: 'about',
        loadComponent: () => import('./../about/about').then(m => m.About),
        title: 'About Us - Blasmusik'
    },
    {
        path: 'youth',
        loadComponent: () => import('./../youth/youth').then(m => m.Youth),
        title: 'Our Youth Group - Blasmusik'
    },
    {
        path: 'imprint',
        loadComponent: () => import('./../imprint/imprint').then(m => m.Imprint),
        title: 'Imprint - Blasmusik'
    },
    {
        path: 'privacy',
        loadComponent: () => import('./../privacy/privacy').then(m => m.Privacy),
        title: 'Privacy Policy - Blasmusik'
    }
];
