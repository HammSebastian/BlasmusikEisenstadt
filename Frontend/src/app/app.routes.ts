import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { NewsListComponent } from './components/news/news-list/news-list.component';
import { NewsDetailComponent } from './components/news/news-detail/news-detail.component';
// Import other components like HomeComponent when created
// import { HomeComponent } from './pages/home/home.component'; // Assuming a HomeComponent might be in pages

export const routes: Routes = [
  // { path: '', component: HomeComponent, pathMatch: 'full' }, // Example default route to a Home component
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: 'news', component: NewsListComponent },
  { path: 'news/:id', component: NewsDetailComponent },

  // Admin routes would be prefixed, e.g., /admin/news
  // { path: 'admin', loadChildren: () => import('./modules/admin/admin.routes').then(m => m.ADMIN_ROUTES), canActivate: [AuthGuard] },


  // Redirect to news list or a dedicated home page if route not found or root path
  { path: '', redirectTo: '/news', pathMatch: 'full' }, // Default to news list for now
  { path: '**', redirectTo: '/news' } // Fallback route to news list
];
