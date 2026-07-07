package com.gwpriso.click_and_collect_backend.commande;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CommandeRequest(
        @NotNull UUID utilisateurId,
        @NotNull UUID magasinId,
        @NotEmpty List<@Valid LigneRequest> lignes
) {
    public record LigneRequest(
            @NotNull UUID produitId,
            @NotNull @Min(1) Integer quantite
    ) {
    }
}