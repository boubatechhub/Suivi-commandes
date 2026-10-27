import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { EnvironmentProviders, LOCALE_ID, makeEnvironmentProviders } from '@angular/core';

/**
 * Formats français pour les pipes (« 12,50 € », dates, nombres).
 * Utilisé dans app.config.ts et dans les tests qui vérifient l'affichage.
 */
export function provideFrenchLocale(): EnvironmentProviders {
  registerLocaleData(localeFr);
  return makeEnvironmentProviders([{ provide: LOCALE_ID, useValue: 'fr-FR' }]);
}
