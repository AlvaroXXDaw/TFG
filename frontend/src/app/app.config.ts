import {LOCALE_ID, ApplicationConfig, provideBrowserGlobalErrorListeners} from '@angular/core';
import {registerLocaleData} from '@angular/common';
import localeEs from '@angular/common/locales/es';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {provideRouter, withInMemoryScrolling} from '@angular/router';

import {routes} from './app.routes';
import {authInterceptor} from './auth.interceptor';
import {apiErrorInterceptor} from './api-error.interceptor';

registerLocaleData(localeEs, 'es');

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideHttpClient(withInterceptors([authInterceptor, apiErrorInterceptor])),
    provideRouter(routes, withInMemoryScrolling({scrollPositionRestoration: 'top'})),
    {provide: LOCALE_ID, useValue: 'es'},
  ],
};
