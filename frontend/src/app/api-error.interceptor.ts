import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, throwError} from 'rxjs';

import {ApiErrorStore} from './api-error.store';
import {AuthStore} from './auth.store';

export const apiErrorInterceptor: HttpInterceptorFn = (request, next) => {
  const errorStore = inject(ApiErrorStore);
  const authStore = inject(AuthStore);
  const router = inject(Router);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 0) {
        errorStore.set('No se pudo conectar con el backend. Revisa si esta levantado.');
      } else if ((error.status === 401 || error.status === 403) && authStore.isLoggedIn()) {
        authStore.logout();
        errorStore.set('Tu sesion ha caducado. Inicia sesion de nuevo.');
        router.navigate(['/login']);
      } else if (error.error?.message) {
        errorStore.set(error.error.message);
      } else {
        errorStore.set(`Error HTTP ${error.status}`);
      }

      return throwError(() => error);
    }),
  );
};
