import {ApplicationConfig, provideZoneChangeDetection} from '@angular/core';
import {provideRouter, withDebugTracing} from '@angular/router';
import {provideClientHydration} from '@angular/platform-browser';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideAnimations} from '@angular/platform-browser/animations';

import {routes} from './app.routes';
import { authInterceptor } from './core/interceptors/auth-interceptor';
import {errorInterceptor} from './core/interceptors/error-interceptor';
import {authGuard} from './core/guards/auth-guard';

export const appConfig: ApplicationConfig = {
    providers: [
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideClientHydration(),
        provideHttpClient(withInterceptors([authInterceptor, errorInterceptor])),
        provideAnimations(),
    ]
};
