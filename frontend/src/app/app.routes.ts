import { Routes } from '@angular/router';

// Chaque page est chargée à la demande (lazy loading) : son code n'est téléchargé
// qu'au moment où l'utilisateur y navigue.
export const routes: Routes = [
  {
    path: '',
    title: 'Accueil | Suivi des commandes',
    loadComponent: () => import('./features/home/home').then((m) => m.Home),
  },
  {
    path: 'catalogue',
    loadChildren: () => import('./features/catalog/catalog.routes').then((m) => m.CATALOG_ROUTES),
  },
  { path: '**', redirectTo: '' },
];
