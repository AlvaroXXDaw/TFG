import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';

import {AuthStore} from '../auth.store';

export const guestOnlyGuard: CanActivateFn = () => {
  const authStore = inject(AuthStore);
  const router = inject(Router);

  if (authStore.isLoggedIn()) {
    return router.createUrlTree(['/']);
  }

  return true;
};
