package com.suivicommandes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'API.
 * <p>
 * {@code @SpringBootApplication} active la configuration automatique et le scan des composants
 * dans ce package et ses sous-packages (catalog, customer, order...).
 */
@SpringBootApplication
public class SuiviCommandesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuiviCommandesApplication.class, args);
    }
}
