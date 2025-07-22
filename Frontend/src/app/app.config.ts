import {ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {provideAuth0} from '@auth0/auth0-angular';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {provideAnimations} from '@angular/platform-browser/animations';
import {AuthTokenInterceptor} from './core/interceptors/auth-token-interceptor';

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({eventCoalescing: true}),
        provideRouter(routes),
        provideHttpClient(withInterceptorsFromDi()),
        provideAnimations(),
        provideAnimationsAsync(),
        provideAuth0({
            domain: 'dev-sebastianhamm.us.auth0.com',
            clientId: 'qCHx3tqkIw4nvqGpNVj287xEk4CvxBtA',

            authorizationParams: {
                redirect_uri: 'http://localhost:4200/private',
                audience: 'https://api.stadtkapelle-eisenstadt.at/',
                scope: 'openid profile email read:admin read:data write:data read:admin',
            },
            /*
            httpInterceptor: {
                allowedList: [
                    {
                        uri: 'http://localhost:8081/api/v1/*',
                        tokenOptions: {
                            authorizationParams: {
                                audience: 'https://api.stadtkapelle-eisenstadt.at/',
                                scope: 'openid profile email read:admin read:data write:data read:admin',
                            },
                        },
                    },
                    {
                        uri: 'https://api.stadtkapelle-eisenstadt.at/api/v1/*',
                        tokenOptions: {
                            authorizationParams: {
                                audience: 'https://api.stadtkapelle-eisenstadt.at/',
                                scope: 'openid profile email read:admin read:data write:data read:admin',
                            },
                        },
                    },
                ],
            }
             */
        }),
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthTokenInterceptor,
            multi: true
        }
    ]
};
