-- Première migration : elle ne crée encore aucune table.
-- Elle permet de vérifier que Flyway est branché et crée sa table d'historique
-- (flyway_schema_history). Les tables métier arriveront à partir du J2 (V2, V3...).
-- Règle : une migration déjà appliquée ne se modifie jamais, on en ajoute une nouvelle.
SELECT 1;
