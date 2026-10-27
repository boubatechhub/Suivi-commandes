import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

/** Page d'accueil provisoire, remplacée par le dashboard au J10. */
@Component({
  selector: 'app-home',
  imports: [RouterLink],
  template: `
    <h1>Bienvenue</h1>
    <p>Commence par le <a routerLink="/catalogue">catalogue des produits</a>.</p>
  `,
})
export class Home {}
