import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptorsFromDi } from '@angular/common/http';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ReactiveFormsModule } from '@angular/forms';

// If not using standalone components for everything and you have NgModules:
// import { HttpClientModule } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(), // Optional: for server-side rendering hydration
    provideHttpClient(withFetch(), withInterceptorsFromDi()), // Configure HttpClient with fetch and DI interceptors
    // importProvidersFrom(HttpClientModule), // Use this if you prefer HttpClientModule and have NgModules
    importProvidersFrom(ReactiveFormsModule), // Needed for standalone components using reactive forms
    provideAnimationsAsync() // For Angular animations
  ]
};
