package com.suivicommandes;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Test de fumée : l'application démarre, se connecte à PostgreSQL et applique les migrations.
 * Risque couvert : une erreur de configuration (datasource, Flyway, Hibernate) qui empêcherait l'API de démarrer.
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class SuiviCommandesApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void contextLoadsAndFlywayAppliesMigrations() {
        Integer applied = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE success", Integer.class);

        assertThat(applied).isGreaterThanOrEqualTo(1);
    }
}
