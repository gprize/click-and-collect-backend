package com.gwpriso.click_and_collect_backend.produit;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ImportRequest(
        @NotNull UUID magasinId,
        @NotEmpty List<@Valid ImportLigneRequest> lignes
) {
}