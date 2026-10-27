import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

/**
 * Composant racine : il ne contient que la structure de la page (en-tête, navigation)
 * et le <router-outlet> où le routeur affiche la page courante.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly appName = 'Suivi des commandes';
}
