import { Routes } from '@angular/router';
import { FRENCH_PAGINATOR_PROVIDER } from '../../shared/material/french-paginator-intl';

/** Routes du catalogue, chargées seulement quand l'utilisateur ouvre /catalogue. */
export const CATALOG_ROUTES: Routes = [
  {
    path: '',
    // Providers de route : disponibles pour toutes les pages du catalogue, et seulement pour elles
    providers: [FRENCH_PAGINATOR_PROVIDER],
    children: [
      {
        path: '',
        title: 'Catalogue | Suivi des commandes',
        loadComponent: () => import('./product-list/product-list').then((m) => m.ProductList),
      },
      {
        path: 'nouveau',
        title: 'Nouveau produit | Suivi des commandes',
        loadComponent: () => import('./product-form/product-form').then((m) => m.ProductForm),
      },
      {
        // Le paramètre :id est transmis à l'input `id` du composant (withComponentInputBinding)
        path: ':id',
        title: 'Modifier un produit | Suivi des commandes',
        loadComponent: () => import('./product-form/product-form').then((m) => m.ProductForm),
      },
    ],
  },
];
