# Suivi des commandes

Application pédagogique full stack de gestion et de suivi des commandes en ligne : **Angular 22** pour le frontend, **Spring Boot 4.1** pour l'API REST, **PostgreSQL 18** pour la base.

Ce n'est pas un simple CRUD : l'application applique des règles métier explicites (transitions de statut contrôlées, stock, délais), garde l'historique de chaque commande et signale les commandes en retard ou prioritaires.

> État : **J1, fondations**. Aucune fonctionnalité métier n'est encore livrée. Voir la [feuille de route](docs/backlog.md).

## Structure du dépôt

```
suivi-commandes/
├── backend/             API Spring Boot (Java 25, Maven Wrapper)
├── frontend/            Application Angular 22
├── docs/                Cahier des charges, diagrammes, backlog, veille Angular, fiches pédagogiques
├── docker-compose.yml   PostgreSQL 18 pour le développement
└── .github/workflows/   Intégration continue
```

## Prérequis

| Outil | Version | Vérification |
|---|---|---|
| Node.js | 24 LTS (au moins 24.15.0) | `node -v` |
| JDK | 25 | `java -version` |
| Docker Desktop | récent, moteur WSL 2 sous Windows | `docker --version` |
| Git | récent | `git --version` |

Maven et Angular CLI n'ont pas besoin d'être installés globalement : le projet utilise `mvnw` et la version locale du CLI.

## Démarrage

### 1. Base de données

```powershell
docker compose up -d
```

PostgreSQL écoute sur `localhost:5432` (base `suivi_commandes`, utilisateur `suivi`, mot de passe `suivi`, valeurs de développement uniquement). Pour les changer, copie `.env.example` en `.env`.

### 2. API

```powershell
cd backend
.\mvnw.cmd spring-boot:run      # Windows
./mvnw spring-boot:run          # macOS / Linux
```

Vérification : <http://localhost:8080/actuator/health> doit répondre `{"status":"UP"}`.

### 3. Frontend

```powershell
cd frontend
npm install
npm start
```

Ouvre <http://localhost:4200>. Les appels vers `/api` sont redirigés vers l'API par le proxy de développement (`proxy.conf.json`).

## Tests et qualité

| Commande | Ce qu'elle vérifie |
|---|---|
| `cd backend` puis `.\mvnw.cmd verify` | Compilation et tests (lance un PostgreSQL jetable : Docker doit tourner) |
| `cd frontend` puis `npm run lint` | Règles ESLint et accessibilité des templates |
| `cd frontend` puis `npx ng test --watch=false` | Tests unitaires Vitest |
| `cd frontend` puis `npm run build` | Build de production |

La CI GitHub Actions exécute ces mêmes contrôles à chaque Pull Request.

## Documentation

- [Cahier des charges](docs/cahier-des-charges.md)
- [Diagrammes](docs/diagrammes/)
- [Backlog et progression](docs/backlog.md)
- [Veille Angular](docs/veille-angular.md)
- [Fiches pédagogiques](docs/fiches/)

## Workflow Git

- `main` est protégée : on n'y pousse jamais directement.
- Une branche par incrément (`feat/j2-api-catalogue`), une Pull Request, une auto-revue, puis fusion.
- Messages de commit au format [Conventional Commits](https://www.conventionalcommits.org/fr/) : `feat(catalog): ...`, `fix(order): ...`, `docs: ...`.
