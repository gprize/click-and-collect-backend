package com.gwpriso.click_and_collect_backend.magasin;

import jakarta.validation.constraints.NotBlank;

public record MagasinRequest(
        @NotBlank String nom,
        @NotBlank String adresse
) {
}