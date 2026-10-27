package com.suivicommandes.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Données reçues pour créer ou modifier un produit.
 * <p>
 * Les annotations Bean Validation sont vérifiées par {@code @Valid} dans le contrôleur,
 * avant d'atteindre le service. Un champ invalide produit une réponse 400.
 * <p>
 * {@code active} est facultatif : absent à la création, il vaut {@code true}.
 */
public record ProductRequest(
        @NotBlank
        @Size(max = 40)
        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "ne doit contenir que des lettres, chiffres et tirets")
        String sku,

        @NotBlank
        @Size(max = 120)
        String name,

        @Size(max = 1000)
        String description,

        @NotNull
        @DecimalMin("0.00")
        @Digits(integer = 8, fraction = 2)
        BigDecimal unitPrice,

        @NotNull
        @PositiveOrZero
        Integer stockAvailable,

        Boolean active) {
}
