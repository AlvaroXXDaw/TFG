import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';

import {AuthStore} from './auth.store';
export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authStore = inject(AuthStore);
  const session = authStore.session();

  if (session?.token) {
    const clonedRequest = request.clone({
      setHeaders: {
        Authorization: `Bearer ${session.token}`,
      },
    });
    return next(clonedRequest);
  }

  return next(request);
};
