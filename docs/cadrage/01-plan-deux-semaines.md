# Plan sur deux semaines — Suivi des commandes

*Version 0.2 du 27 octobre 2026 · Complète [00-cadrage-initial.md](00-cadrage-initial.md)*

## 1. Décisions validées

| Sujet | Décision |
|---|---|
| Nom | **Suivi des commandes** (dépôt suggéré : `suivi-commandes`, package Java : `com.suivicommandes`) |
| Stack | Recommandations acceptées : Angular 22.2.x, Node 24 LTS, TypeScript 6.0.x, RxJS 7.8, Spring Boot 4.1.2, Java 25 LTS, PostgreSQL 18 (Docker), session + CSRF, Reactive Forms en principal, Angular Material, stock simple |
| Langue | Code en anglais, **commentaires en français**, libellés de l'interface en français. Statuts API en anglais (`DRAFT`, `CONFIRMED`, `IN_PREPARATION`, `ON_HOLD`, `SHIPPED`, `DELIVERED`, `CANCELLED`), affichés en français |
| Machine | Windows, Node 22.17 → à mettre à jour |
| Dépôt | Public, créé par Bouba |
| Charte | Les 4 couleurs proposées (bleu nuit, sarcelle, ambre, ardoise) + rouge d'erreur. Modifiable plus tard |
| Délai | **Deux semaines** |

## 2. Ce que deux semaines changent

Deux semaines suffisent pour un MVP solide **si tu y consacres plusieurs heures par jour** et si on reste strict sur le périmètre. Pour tenir :

- Chaque journée = **un incrément vertical** livré par une PR, avec une fiche pédagogique courte (l'essentiel des 10 rubriques, sans remplissage).
- J'écris le code avec toi et je l'explique ; **toi, tu fais l'exercice du jour** et tu réécris un morceau seul. C'est ce qui fixe les notions.
- La veille Angular (29 questions) est remplie au fil des jours, chaque réponse pointant vers le code du projet.
- Coupé du MVP de deux semaines : alertes planifiées, notifications temps réel, simulation, dashboard personnalisable, export CSV, mode sombre. Le score de priorité et le dashboard sont gardés en version simple.

## 3. Planning (10 jours ouvrés)

| Jour | Livrable | Notions clés |
|---|---|---|
| **J1** | Installation Windows, dépôt, Docker PostgreSQL, squelettes Spring Boot et Angular, CI GitHub Actions, README | Node/npm/CLI/compilateur/bundler, structure des projets, Git, branches, PR |
| **J2** | API Catalogue produits (entité, DTO, validation, erreurs, Flyway, tests) | Couches Spring, JPA, Bean Validation, ProblemDetail, JUnit |
| **J3** | Écrans Catalogue (liste, détail, formulaire) | Composants, bindings, `@if`/`@for`, services, DI, HttpClient, Reactive Forms |
| **J4** | Clients + recherche paginée | Pagination/tri, router, lazy loading, `debounceTime`/`distinctUntilChanged`/`switchMap` |
| **J5** | Authentification et rôles | Spring Security, session, CSRF, intercepteurs, gardes de route, tests d'autorisation |
| **J6** | Création de commande (API) | Relations JPA, transactions, référence unique, copie des prix, stock |
| **J7** | Création de commande (écran) | `FormArray`, validateurs, `input()`/`output()` et `@Input`/`@Output`, cycle de vie |
| **J8** | Cycle de vie + timeline | Moteur de transitions testable, 409, verrouillage optimiste, unions TypeScript |
| **J9** | Recherche avancée + retards + score de priorité | JPA Specifications, `combineLatest`, query params, logique métier pure |
| **J10** | Dashboard + consolidation → tag `v1.0.0` | Agrégations, `LoadState<T>`, états chargement/erreur/vide, bilan pédagogique |

Les week-ends servent de tampon : rattraper un retard, refaire les exercices, compléter la veille.

## 4. Installation sur Windows (à faire avant J1)

1. **Node.js 24 LTS** : télécharge l'installateur « LTS » sur [nodejs.org](https://nodejs.org/) et installe-le par-dessus la 22.17. Variante si tu veux pouvoir changer de version plus tard : `nvm-windows`.
2. **Java 25 (JDK)** : Eclipse Temurin 25 sur [adoptium.net](https://adoptium.net/), en cochant « Set JAVA_HOME ».
3. **Docker Desktop** avec le moteur WSL 2 (Docker le propose à l'installation).
4. **Git for Windows**, si ce n'est pas déjà fait.
5. Pas besoin d'installer Maven ni Angular CLI : le projet utilisera `mvnw` et `npx ng`.

Ensuite, ouvre un **nouveau** terminal PowerShell et envoie-moi la sortie de :

```powershell
node -v; npm -v; java -version; docker --version; git --version
```

Attendu : `node` en v24.x (au moins 24.15.0), `java` en 25.

## 5. Trello

**Option A (recommandée)** : connecte le connecteur Trello à Claude. Je pourrai alors lire ton tableau et, **avec ton accord**, créer les cartes et les déplacer au fil des jours.

**Option B** : tu crées toi-même un tableau « Suivi des commandes » avec les colonnes Backlog, À faire, En cours, À tester, Terminé, puis tu copies les cartes ci-dessous.

Cartes de départ (une par jour, à mettre dans Backlog, J1 dans À faire) :

- J1 · Fondations : installation, dépôt, Docker, squelettes, CI
- J2 · API Catalogue produits
- J3 · Écrans Catalogue
- J4 · Clients et recherche paginée
- J5 · Authentification et rôles
- J6 · Commande : API de création
- J7 · Commande : écran de création
- J8 · Cycle de vie et timeline
- J9 · Recherche avancée, retards, score de priorité
- J10 · Dashboard et version 1.0
- Évolutions (hors deux semaines) : alertes, notifications, simulation, export CSV, mode sombre
