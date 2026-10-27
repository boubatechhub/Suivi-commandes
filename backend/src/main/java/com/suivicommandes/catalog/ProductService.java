package com.suivicommandes.catalog;

import com.suivicommandes.catalog.dto.ProductRequest;
import com.suivicommandes.catalog.dto.ProductResponse;
import com.suivicommandes.common.error.ConflictException;
import com.suivicommandes.common.error.NotFoundException;
import com.suivicommandes.common.web.PageResponse;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Règles métier du catalogue.
 * <p>
 * Le contrôleur ne fait que traduire HTTP ↔ Java ; c'est ici que l'on décide
 * si une opération est permise. On peut ainsi tester ces règles sans HTTP.
 * <p>
 * {@code @Transactional(readOnly = true)} au niveau de la classe : chaque méthode publique
 * s'exécute dans une transaction en lecture seule, sauf celles qui redéclarent {@code @Transactional}.
 */
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // Injection par constructeur : la dépendance est obligatoire, explicite et facile à remplacer dans un test
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageResponse<ProductResponse> search(String query, Pageable pageable) {
        Page<Product> page = (query == null || query.isBlank())
                ? productRepository.findAll(pageable)
                : productRepository.findBySkuContainingIgnoreCaseOrNameContainingIgnoreCase(
                        query.trim(), query.trim(), pageable);
        return PageResponse.from(page, ProductResponse::from);
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(getProduct(id));
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        String sku = normalizeSku(request.sku());
        if (productRepository.existsBySkuIgnoreCase(sku)) {
            throw new ConflictException("Le SKU " + sku + " est déjà utilisé.");
        }
        // Un produit est actif par défaut si le champ n'est pas fourni
        boolean active = request.active() == null || request.active();
        Product product = new Product(sku, request.name().trim(), request.description(),
                request.unitPrice(), request.stockAvailable(), active);
        return ProductResponse.from(productRepository.save(product));
    }

    /**
     * Pas besoin d'appeler save() : l'entité chargée est « managée » par JPA,
     * ses modifications sont écrites en base à la fin de la transaction.
     */
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getProduct(id);
        if (!product.getSku().equalsIgnoreCase(normalizeSku(request.sku()))) {
            throw new ConflictException("Le SKU d'un produit ne peut pas être modifié.");
        }
        boolean active = request.active() == null ? product.isActive() : request.active();
        product.updateDetails(request.name().trim(), request.description(), request.unitPrice(),
                request.stockAvailable(), active);
        return ProductResponse.from(product);
    }

    private Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produit " + id + " introuvable."));
    }

    private static String normalizeSku(String sku) {
        return sku.trim().toUpperCase(Locale.ROOT);
    }
}
