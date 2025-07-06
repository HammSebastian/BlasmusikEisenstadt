import {HttpInterceptorFn, HttpErrorResponse} from '@angular/common/http';
import {inject} from '@angular/core';
import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {NotificationService} from '../services/essentials/notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const notificationService = inject(NotificationService);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = 'Ein unbekannter Fehler ist aufgetreten.';

            if (error.error instanceof ErrorEvent) {
                // Client-side error
                errorMessage = `Error: ${error.error.message}`;
            } else {
                // Server-side error
                switch (error.status) {
                    case 401:
                        errorMessage = 'Nicht angemeldet';
                        break;
                    case 403:
                        errorMessage = 'Zugriff verweigert';
                        break;
                    case 404:
                        errorMessage = 'Nicht gefunden';
                        break;
                    case 500:
                        errorMessage = 'Interner Serverfehler';
                        break;
                    default:
                        errorMessage = error.error?.message ?? errorMessage;
                }
            }

            notificationService.showError(errorMessage);
            return throwError(() => error);
        })
    );
};
