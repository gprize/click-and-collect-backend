package com.gwpriso.click_and_collect_backend.produit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProduitRequest(

        @NotNull
        UUID magasinId,

        @NotBlank
        String nom,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal prix,

        @NotNull
        @Min(0)
        Integer quantiteStock
) {
}