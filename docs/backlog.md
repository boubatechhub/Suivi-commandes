# Backlog et progression

Un incrément par jour ouvré, livré par une Pull Request. Légende : ✅ terminé · 🔄 en cours · ⬜ à faire.

| Jour | Incrément | Critères d'acceptation principaux | État |
|---|---|---|---|
| J1 | Fondations | API démarre et `/actuator/health` = UP ; Flyway appliqué ; Angular build + tests + lint OK ; CI verte | ✅ (PR #1) |
| J2 | API Catalogue produits | CRUD produits ; SKU unique (409) ; validation (400 ProblemDetail) ; tests service + contrôleur + persistance | 🔄 |
| J3 | Écrans Catalogue | Liste, détail, création, modification ; erreurs serveur affichées ; tests composant + service HTTP | ⬜ |
| J4 | Clients + recherche paginée | Recherche avec délai de saisie, tri, pagination, filtres dans l'URL ; archivage | ⬜ |
| J5 | Authentification et rôles | Connexion/déconnexion ; 401/403 ; CSRF ; règles par rôle testées | ⬜ |
| J6 | Commande : API de création | Référence unique ; prix copiés ; total serveur ; produits inactifs et clients archivés refusés | ⬜ |
| J7 | Commande : écran de création | Formulaire avec lignes dynamiques, validation, total en direct | ⬜ |
| J8 | Cycle de vie + timeline | Transitions selon le diagramme ; 409 sinon ; stock réservé/libéré ; événements avec auteur ; verrouillage optimiste | ⬜ |
| J9 | Recherche avancée, retards, score | Filtres combinés ; règles R9-R10 ; score avec son détail | ⬜ |
| J10 | Tableau de bord + v1.0.0 | Indicateurs justes sur jeu de données ; états chargement/erreur/vide ; tag v1.0.0 | ⬜ |

## Après la v1.0

Alertes planifiées, notifications temps réel (SSE), simulation « et si ? », export CSV, mode sombre, tableau de bord personnalisable.

## Journal

| Date | Événement |
|---|---|
| 27/10/2026 | Cadrage validé, dépôt créé, J1 démarré |
| 27/10/2026 | Veille Angular alignée sur les 29 questions officielles |
| 27/10/2026 | J1 livré (PR #1, CI verte) ; J2 API Catalogue : 13 tests verts |
