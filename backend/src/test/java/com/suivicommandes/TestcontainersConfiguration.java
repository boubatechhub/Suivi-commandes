package com.suivicommandes;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Démarre un PostgreSQL 18 jetable dans Docker pour les tests.
 * <p>
 * {@code @ServiceConnection} branche automatiquement la datasource Spring sur ce conteneur :
 * pas besoin de recopier l'URL, l'utilisateur ou le mot de passe.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        return new PostgreSQLContainer("postgres:18");
    }
}
