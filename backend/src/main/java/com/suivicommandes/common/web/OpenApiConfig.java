package com.suivicommandes.common.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Informations générales affichées en tête de la documentation Swagger UI. */
@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI suiviCommandesOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Suivi des commandes - API")
                .description("API REST de gestion et de suivi des commandes. Erreurs au format ProblemDetail (RFC 9457).")
                .version("0.2.0"));
    }
}
