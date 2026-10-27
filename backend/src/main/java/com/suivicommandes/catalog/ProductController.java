package com.suivicommandes.catalog;

import com.suivicommandes.catalog.dto.ProductRequest;
import com.suivicommandes.catalog.dto.ProductResponse;
import com.suivicommandes.common.web.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Point d'entrée HTTP du catalogue.
 * <p>
 * Responsabilité limitée : lire la requête, valider sa forme ({@code @Valid}),
 * appeler le service, choisir le code HTTP. Aucune règle métier ici.
 * <p>
 * Pas d'endpoint DELETE : un produit déjà commandé doit rester en base pour l'historique.
 * On le désactive ({@code active = false}) avec PUT.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Produits", description = "Catalogue des produits")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Rechercher des produits",
            description = "Recherche par SKU ou nom (paramètre q), paginée et triable : ?page=0&size=20&sort=name,asc")
    public PageResponse<ProductResponse> search(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.search(q, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consulter un produit")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Créer un produit")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.create(request);
        // 201 Created + en-tête Location qui indique l'URL de la nouvelle ressource
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un produit (le SKU ne change pas)")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }
}
