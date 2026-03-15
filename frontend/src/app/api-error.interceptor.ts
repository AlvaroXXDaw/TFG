import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {catchError, throwError} from 'rxjs';

import {ApiErrorStore} from './api-error.store';

export const apiErrorInterceptor: HttpInterceptorFn = (request, next) => {
  const errorStore = inject(ApiErrorStore);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 0) {
        errorStore.set('No se pudo conectar con el backend. Revisa si está levantado.');
      } else if (error.error?.message) {
        errorStore.set(error.error.message);
      } else {
        errorStore.set(`Error HTTP ${error.status}`);
      }

      return throwError(() => error);
    }),
  );
};
