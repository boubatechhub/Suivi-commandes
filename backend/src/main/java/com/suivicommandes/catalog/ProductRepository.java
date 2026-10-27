package com.suivicommandes.catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès aux données des produits.
 * <p>
 * Spring Data génère l'implémentation à partir du nom des méthodes :
 * aucune requête SQL n'est écrite à la main ici.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuIgnoreCase(String sku);

    /** Recherche par SKU ou par nom, sans tenir compte de la casse, avec pagination et tri. */
    Page<Product> findBySkuContainingIgnoreCaseOrNameContainingIgnoreCase(String sku, String name, Pageable pageable);
}
