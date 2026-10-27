import { Provider } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';

/** Libellés français du paginateur Angular Material (anglais par défaut). */
export function frenchPaginatorIntl(): MatPaginatorIntl {
  const intl = new MatPaginatorIntl();
  intl.itemsPerPageLabel = 'Éléments par page';
  intl.nextPageLabel = 'Page suivante';
  intl.previousPageLabel = 'Page précédente';
  intl.firstPageLabel = 'Première page';
  intl.lastPageLabel = 'Dernière page';
  intl.getRangeLabel = (page, pageSize, length) => {
    if (length === 0) {
      return '0 sur 0';
    }
    const start = page * pageSize + 1;
    const end = Math.min((page + 1) * pageSize, length);
    return `${start} – ${end} sur ${length}`;
  };
  return intl;
}

/**
 * Fourni au niveau des routes qui affichent un paginateur, et non dans app.config.ts :
 * sinon le code de Material serait chargé dès le démarrage, même sur la page d'accueil.
 */
export const FRENCH_PAGINATOR_PROVIDER: Provider = {
  provide: MatPaginatorIntl,
  useFactory: frenchPaginatorIntl,
};
