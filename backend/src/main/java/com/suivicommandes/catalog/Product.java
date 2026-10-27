package com.suivicommandes.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entité JPA : représente une ligne de la table {@code product}.
 * <p>
 * Elle ne sort jamais de l'API : les contrôleurs exposent des DTO
 * ({@link com.suivicommandes.catalog.dto.ProductResponse}).
 * Pas de setters publics : on modifie un produit par des méthodes qui ont un sens métier.
 */
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String sku;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "stock_available", nullable = false)
    private int stockAvailable;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Constructeur exigé par JPA, inutilisable ailleurs. */
    protected Product() {
    }

    public Product(String sku, String name, String description, BigDecimal unitPrice, int stockAvailable,
            boolean active) {
        this.sku = sku;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockAvailable = stockAvailable;
        this.active = active;
    }

    /** Met à jour les informations modifiables du produit (le SKU, identifiant métier, ne change pas). */
    public void updateDetails(String name, String description, BigDecimal unitPrice, int stockAvailable, boolean active) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stockAvailable = stockAvailable;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
