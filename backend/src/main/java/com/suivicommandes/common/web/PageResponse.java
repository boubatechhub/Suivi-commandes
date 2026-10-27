package com.suivicommandes.common.web;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

/**
 * Format stable des listes paginées renvoyées par l'API.
 * <p>
 * On n'expose pas directement {@link Page} : sa structure JSON dépend de Spring Data
 * et pourrait changer d'une version à l'autre, ce qui casserait le frontend.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <E, T> PageResponse<T> from(Page<E> page, Function<E, T> mapper) {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }
}
