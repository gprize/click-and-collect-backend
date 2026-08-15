package com.gwpriso.click_and_collect_backend.produit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ImportLigneRequest(
        @NotBlank String nom,
        @NotNull BigDecimal prix,
        @NotNull Integer quantiteStock
) {
}