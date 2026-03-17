import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';

import {AuthStore} from '../auth.store';

export const authRequiredGuard: CanActivateFn = () => {
  const authStore = inject(AuthStore);
  const router = inject(Router);

  if (authStore.isUserRole()) {
    return true;
  }

  return router.createUrlTree(['/login']);
};
