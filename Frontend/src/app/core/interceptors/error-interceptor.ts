import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';
import {inject} from '@angular/core';
import {NotificationService} from '../services/notification';


export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const notificationService = inject(NotificationService);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = 'An unexpected error occurred';

            if (error.error instanceof ErrorEvent) {
                // Client-side error
                errorMessage = `Error: ${error.error.message}`;
            } else {
                // Server-side error
                switch (error.status) {
                    case 401:
                        errorMessage = 'Unauthorized access';
                        break;
                    case 403:
                        errorMessage = 'Access forbidden';
                        break;
                    case 404:
                        errorMessage = 'Resource not found';
                        break;
                    case 500:
                        errorMessage = 'Internal server error';
                        break;
                    default:
                        errorMessage = error.error?.message || errorMessage;
                }
            }

            notificationService.showError(errorMessage);
            return throwError(() => error);
        })
    );
};
