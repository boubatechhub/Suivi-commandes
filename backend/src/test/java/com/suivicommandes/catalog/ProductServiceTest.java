package com.suivicommandes.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.suivicommandes.catalog.dto.ProductRequest;
import com.suivicommandes.catalog.dto.ProductResponse;
import com.suivicommandes.common.error.ConflictException;
import com.suivicommandes.common.error.NotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests unitaires des règles du catalogue : pas de Spring, pas de base.
 * Le repository est remplacé par un « mock » Mockito dont on décide les réponses.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void create_normaliseLeSkuEtActiveLeProduitParDefaut() {
        when(productRepository.existsBySkuIgnoreCase("TASSE-01")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse created = productService.create(request(" tasse-01 ", null));

        assertThat(created.sku()).isEqualTo("TASSE-01");
        assertThat(created.active()).isTrue();
    }

    @Test
    void create_refuseUnSkuDejaUtilise() {
        when(productRepository.existsBySkuIgnoreCase("TASSE-01")).thenReturn(true);

        assertThatThrownBy(() -> productService.create(request("tasse-01", null)))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("TASSE-01");
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_produitInexistant_leveNotFound() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(42L, request("TASSE-01", true)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_refuseUnChangementDeSku() {
        Product existing = new Product("TASSE-01", "Tasse", null, new BigDecimal("12.50"), 5, true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> productService.update(1L, request("BOL-01", true)))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void update_conserveLEtatActifSiNonFourni() {
        Product existing = new Product("TASSE-01", "Tasse", null, new BigDecimal("12.50"), 5, false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        ProductResponse updated = productService.update(1L, request("TASSE-01", null));

        assertThat(updated.active()).isFalse();
        assertThat(updated.name()).isEqualTo("Tasse en grès");
    }

    private static ProductRequest request(String sku, Boolean active) {
        return new ProductRequest(sku, "Tasse en grès", "Faite main", new BigDecimal("12.50"), 10, active);
    }
}
