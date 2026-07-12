package com.gwpriso.click_and_collect_backend.produit;

import java.math.BigDecimal;
import java.util.UUID;

public record ProduitResponse(
        UUID id,
        UUID magasinId,
        String nom,
        BigDecimal prix,
        boolean disponible
) {
    public static ProduitResponse from(Produit produit) {
        return new ProduitResponse(
                produit.getId(),
                produit.getMagasin().getId(),
                produit.getNom(),
                produit.getPrix(),
                produit.getQuantiteStock() > 0
        );
    }
}