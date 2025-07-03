import {Routes} from '@angular/router';

export const authRoutes: Routes = [
    {
        path: 'login',
        loadComponent: () => import('./login/login').then(m => m.Login),
        title: 'Login - Blasmusik'
    },
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    }
];
