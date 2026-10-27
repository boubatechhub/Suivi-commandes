# Fiche J1 : fondations du projet

## 1. Besoin utilisateur

Avant toute fonctionnalité, il faut pouvoir **installer, lancer et tester** le projet de façon identique sur ton PC Windows, sur une autre machine et dans la CI.

## 2. Problème technique

Trois programmes doivent coopérer (Angular, Spring Boot, PostgreSQL), chacun avec ses outils et ses versions. Sans fondations claires, on perd des heures sur des « ça marche chez moi ».

## 3. Concepts à apprendre

- Le rôle de chaque outil : Node.js, npm, Angular CLI, TypeScript, compilateur Angular, bundler (esbuild), serveur de dev (Vite), JDK, Maven, Docker.
- La structure d'un projet Angular et d'un projet Spring Boot.
- Les migrations de base de données (Flyway).
- La configuration par variables d'environnement.
- L'intégration continue.

## 4. Solution mise en place

| Besoin | Solution | Fichier |
|---|---|---|
| Base de données identique partout | PostgreSQL 18 dans Docker | `docker-compose.yml` |
| Même version de Maven pour tous | Maven Wrapper | `backend/mvnw`, `backend/mvnw.cmd` |
| Schéma versionné | Flyway | `backend/src/main/resources/db/migration/V1__init.sql` |
| Pas de secret dans le code | Variables `${DB_URL:...}` avec valeur de dev par défaut | `backend/src/main/resources/application.yml` |
| Tests sur un vrai PostgreSQL | Testcontainers + `@ServiceConnection` | `backend/src/test/java/com/suivicommandes/` |
| Appels `/api` sans CORS en dev | Proxy du serveur de dev | `frontend/proxy.conf.json` |
| Charte graphique centralisée | Variables CSS | `frontend/src/styles.scss` |
| Pages chargées à la demande | `loadComponent` | `frontend/src/app/app.routes.ts` |
| Contrôle automatique | GitHub Actions | `.github/workflows/ci.yml` |
| Fins de ligne correctes sous Windows | `.gitattributes` | `.gitattributes` |

## 5. Le code expliqué

**`application.yml`**

```yaml
url: ${DB_URL:jdbc:postgresql://localhost:5432/suivi_commandes}
```

Syntaxe `${VARIABLE:valeur_par_défaut}` : Spring lit la variable d'environnement `DB_URL` et, si elle n'existe pas, utilise la valeur après les deux-points. En dev, rien à configurer ; en production, on fournit la variable.

```yaml
ddl-auto: validate
```

Hibernate pourrait créer les tables tout seul (`update`), mais on perd alors le contrôle et l'historique du schéma. Avec `validate`, il vérifie seulement que les entités correspondent aux tables créées par Flyway, et refuse de démarrer sinon.

```yaml
open-in-view: false
```

Par défaut, Spring garde la session JPA ouverte jusqu'à la fin de la réponse HTTP, ce qui masque des requêtes SQL cachées. On le désactive pour être explicite.

**`TestcontainersConfiguration.java`** : `@ServiceConnection` sur le conteneur dit à Spring Boot « connecte la datasource à ce PostgreSQL ». Le test démarre une vraie base jetable, applique les migrations et vérifie la table `flyway_schema_history`.

**`app.routes.ts`** : `loadComponent: () => import('./features/home/home')` crée un fichier JavaScript séparé (`chunk-...js` dans la sortie du build), téléchargé seulement à la visite de la page. C'est le **lazy loading**.

## 6. Alternatives

| Choix | Alternative | Pourquoi pas maintenant |
|---|---|---|
| Testcontainers | Base H2 en mémoire | H2 n'est pas PostgreSQL : des requêtes valides sur l'un échouent sur l'autre |
| Flyway | Liquibase | Équivalent ; Flyway, en SQL pur, est plus simple à lire |
| Proxy de dev | Configurer CORS | CORS reste nécessaire si le front et l'API sont sur des domaines différents en production ; on le traitera au J5 |
| Monorepo | Deux dépôts | Un seul développeur, des changements souvent liés : un dépôt simplifie les PR |

## 7. Tests écrits

| Test | Scénario | Résultat attendu | Risque couvert |
|---|---|---|---|
| `SuiviCommandesApplicationTests` | Démarrer l'API sur un PostgreSQL 18 jetable | Contexte chargé, au moins une migration réussie | Mauvaise configuration datasource, Flyway ou Hibernate |
| `app.spec.ts` : crée le composant racine | Instancier `App` | Instance non nulle | Erreur d'import ou de dépendance |
| `app.spec.ts` : affiche le nom | Rendre `App` | `.brand` contient « Suivi des commandes » | Régression du template |

Résultats vérifiés le 27/10/2026 : backend 1/1 réussi, frontend 2/2 réussis, lint sans erreur, build OK.

## 8. Erreurs fréquentes

- **`node` trop ancien** : Angular 22 refuse de démarrer avec Node < 22.22.3. Vérifie `node -v`.
- **`JAVA_HOME` pointe vers un autre JDK** : `mvnw` utilise `JAVA_HOME`. Vérifie avec `.\mvnw.cmd -v`.
- **Docker Desktop arrêté** : les tests backend échouent avec « Could not find a valid Docker environment ».
- **Port 5432 déjà pris** : un PostgreSQL installé sur Windows occupe le port. Arrête-le ou change le port dans `docker-compose.yml`.
- **`mvnw` avec des fins de ligne Windows** : le script échoue en CI. Le `.gitattributes` l'empêche.

## 9. Exercice

1. Lance la base, l'API et le frontend en suivant le README, sans regarder cette fiche.
2. Ajoute une migration `V2__exercice.sql` qui crée une table `note(id bigserial primary key, texte varchar(200))`. Relance l'API et retrouve ta migration dans `flyway_schema_history` (`docker exec -it suivi-commandes-db psql -U suivi -d suivi_commandes`).
3. Modifie ensuite `V2__exercice.sql` et relance l'API. Explique le message d'erreur. Supprime ta migration et la base (`docker compose down -v`) avant de continuer.
4. Côté Angular, ajoute une page « À propos » chargée à la demande, avec un lien dans l'en-tête. Vérifie dans la sortie de `npm run build` qu'un nouveau *lazy chunk* apparaît.

## 10. Questions de révision

- Pourquoi `ng serve` n'a-t-il pas besoin de Webpack dans ce projet ?
- Quelle différence entre Node.js et le navigateur dans l'exécution d'une application Angular ?
- Pourquoi ne jamais modifier une migration Flyway déjà appliquée ?
