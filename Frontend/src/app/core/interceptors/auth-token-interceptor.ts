import {inject, Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {from, Observable} from 'rxjs';
import {catchError, switchMap} from 'rxjs/operators';
import {AuthService} from '@auth0/auth0-angular';

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {

    private readonly auth = inject(AuthService)

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const allowedUrls = [
            'http://localhost:8081/api/v1/',
            'https://api.stadtkapelle-eisenstadt.at/api/v1/'
        ];

        const isAllowedUrl = allowedUrls.some(url => request.url.startsWith(url));

        if (!isAllowedUrl) {
            return next.handle(request);
        }

        return from(this.auth.getAccessTokenSilently({
            authorizationParams: {
                audience: 'https://api.stadtkapelle-eisenstadt.at/',
                scope: 'openid profile email read:admin read:data write:data',
            }
        })).pipe(
            switchMap(token => {
                const authReq = request.clone({
                    headers: request.headers.set('Authorization', `Bearer ${token}`)
                });
                return next.handle(authReq);
            }),
            catchError(error => {
                return next.handle(request);
            })
        );
    }
}

