import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';

import {AuthStore} from '../auth.store';

export const adminOnlyGuard: CanActivateFn = () => {
  const authStore = inject(AuthStore);
  const router = inject(Router);

  if (!authStore.isLoggedIn()) {
    return router.createUrlTree(['/login']);
  }

  if (authStore.isAdmin()) {
    return true;
  }

  return router.createUrlTree(['/']);
};
