package com.suivicommandes.catalog.dto;

import com.suivicommandes.catalog.Product;
import java.math.BigDecimal;
import java.time.Instant;

/** Représentation d'un produit renvoyée par l'API. */
public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,
        BigDecimal unitPrice,
        int stockAvailable,
        boolean active,
        Instant createdAt,
        Instant updatedAt) {

    /** Conversion entité → DTO, écrite à la main pour voir exactement ce qui est exposé. */
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStockAvailable(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}
