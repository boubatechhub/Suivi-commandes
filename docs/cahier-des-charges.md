# Cahier des charges fonctionnel

*Version 1.0 du 27/10/2026. Source : [cadrage initial](cadrage/00-cadrage-initial.md) et [plan sur deux semaines](cadrage/01-plan-deux-semaines.md).*

## 1. Contexte

Une petite boutique en ligne doit suivre ses commandes de la création à la livraison. Aujourd'hui, l'équipe ne voit pas facilement quelles commandes stagnent, lesquelles sont en retard et dans quel ordre les traiter. **Suivi des commandes** est un poste de pilotage qui applique des règles métier explicites et explique ses décisions.

Hors périmètre : paiement réel, intégration transporteur, envoi d'e-mails, portail client.

## 2. Utilisateurs

| Rôle | Droits |
|---|---|
| Opérateur (`OPERATOR`) | Crée et suit les commandes, fait avancer les statuts, gère les clients, consulte le tableau de bord |
| Administrateur (`ADMIN`) | Droits de l'opérateur + gestion du catalogue et annulation d'une commande en préparation |

Les droits sont vérifiés **côté serveur**. Masquer un bouton dans l'interface n'est pas une mesure de sécurité.

## 3. Scénarios d'utilisation

1. Le matin, l'opérateur ouvre le tableau de bord : nombre de commandes en retard, en risque, et la liste à traiter en priorité.
2. Il ouvre une commande, consulte sa timeline et la fait passer de « Confirmée » à « En préparation ».
3. Il tente une transition interdite : l'application refuse et indique les transitions possibles.
4. Il bloque une commande avec un motif ; l'événement est enregistré avec son nom et l'heure.
5. Il recherche les commandes d'un client, en retard, sur une période donnée.

## 4. Statuts

| Code API | Libellé affiché |
|---|---|
| `DRAFT` | Brouillon |
| `CONFIRMED` | Confirmée |
| `IN_PREPARATION` | En préparation |
| `ON_HOLD` | Bloquée |
| `SHIPPED` | Expédiée |
| `DELIVERED` | Livrée |
| `CANCELLED` | Annulée |

Transitions autorisées : voir le [diagramme d'états](diagrammes/statuts-commande.md).

## 5. Règles métier

| # | Règle |
|---|---|
| R1 | La référence est générée par le serveur, unique, au format `CMD-2026-00042`. |
| R2 | Une commande a au moins une ligne ; chaque quantité est supérieure ou égale à 1. |
| R3 | Le prix unitaire et le nom du produit sont copiés dans la ligne à la création. Le total est calculé par le serveur. |
| R4 | Les lignes ne sont modifiables qu'en `DRAFT`. |
| R5 | Toute transition passe par le moteur de transitions ; une transition non prévue est refusée (HTTP 409). |
| R6 | L'annulation exige un motif, est interdite à partir de `SHIPPED`, et est réservée à l'admin en `IN_PREPARATION`. Une commande sortant de `ON_HOLD` reprend son statut précédent. |
| R7 | La confirmation réserve le stock et échoue si le stock est insuffisant ; l'annulation d'une commande confirmée libère le stock. L'opération est atomique (une transaction). |
| R8 | Chaque création, modification et transition produit un événement horodaté avec son auteur. Les événements ne sont jamais modifiés. |
| R9 | Une commande est en retard si sa date promise est dépassée et qu'elle n'est ni livrée ni annulée. |
| R10 | Une commande est en risque si elle reste dans un statut au-delà du seuil configuré, ou si sa date promise est à moins de 48 h sans expédition. |
| R11 | Un client archivé ou un produit inactif ne peut pas figurer dans une nouvelle commande. |
| R12 | Deux modifications concurrentes d'une même commande ne s'écrasent pas : la seconde est refusée (HTTP 409, verrouillage optimiste). |

## 6. Périmètre

**MVP (deux semaines)** : authentification et rôles, catalogue avec stock simple, clients, création et modification de commandes, moteur de transitions, timeline, recherche multicritères paginée, retards, risques, score de priorité simple, tableau de bord.

**Reporté** : alertes planifiées, notifications temps réel, simulation de scénarios, export CSV, mode sombre, tableau de bord personnalisable.

## 7. Exigences non fonctionnelles

- Interface en français, responsive, conforme aux bases d'accessibilité (contrastes AA, navigation clavier, statut jamais signalé par la seule couleur).
- Erreurs d'API au format `ProblemDetail` (RFC 9457).
- Aucun secret dans le dépôt.
- Installation reproductible avec Docker Compose.
- CI verte obligatoire avant chaque fusion dans `main`.
