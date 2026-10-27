package com.suivicommandes.catalog;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.suivicommandes.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests d'intégration de l'API produits : toute la chaîne est réelle
 * (contrôleur → validation → service → JPA → PostgreSQL 18 dans Docker).
 * {@code @Transactional} annule les écritures après chaque test : les tests restent indépendants.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
@Transactional
class ProductApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void creerUnProduit_renvoie201AvecLocationEtSkuNormalise() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("tasse-01", "12.50")))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/products/")))
                .andExpect(jsonPath("$.sku").value("TASSE-01"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.unitPrice").value(12.50));
    }

    @Test
    void creerUnProduitInvalide_renvoie400AvecLesChampsEnErreur() throws Exception {
        String invalid = """
                {"sku": "avec espace", "name": "", "unitPrice": -1, "stockAvailable": 3}
                """;

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Données invalides"))
                .andExpect(jsonPath("$.errors.sku").exists())
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.unitPrice").exists());
    }

    @Test
    void creerUnSkuEnDouble_memeAvecUneAutreCasse_renvoie409() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("BOL-01", "9.90")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("bol-01", "9.90")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value(containsString("BOL-01")));
    }

    @Test
    void rechercher_filtreParNomOuSkuEtPagine() throws Exception {
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("VASE-01", "30")));
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("VASE-02", "35")));
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON).content(json("ASSIETTE-01", "8")));

        mockMvc.perform(get("/api/products").param("q", "vase").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void trierSurUnChampInconnu_renvoie400() throws Exception {
        mockMvc.perform(get("/api/products").param("sort", "inconnu"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consulterUnProduitInexistant_renvoie404() throws Exception {
        mockMvc.perform(get("/api/products/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void desactiverUnProduit() throws Exception {
        String body = mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content(json("PLAT-01", "25")))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.id");

        String update = """
                {"sku": "PLAT-01", "name": "Plat", "unitPrice": 25, "stockAvailable": 0, "active": false}
                """;
        mockMvc.perform(put("/api/products/{id}", id).contentType(MediaType.APPLICATION_JSON).content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.stockAvailable").value(0));
    }

    private static String json(String sku, String price) {
        return """
                {"sku": "%s", "name": "Produit %s", "unitPrice": %s, "stockAvailable": 10}
                """.formatted(sku, sku, price);
    }
}
