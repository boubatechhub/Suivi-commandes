import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app.routes';
import { provideFrenchLocale } from './core/i18n/french-locale';

/** Providers disponibles dans toute l'application. */
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    // withComponentInputBinding : les paramètres de route (:id) arrivent dans les input() des composants
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(),
    provideFrenchLocale(),
  ],
};
