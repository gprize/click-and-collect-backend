package com.gwpriso.click_and_collect_backend.magasin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MagasinRequest(
        @NotBlank String nom,
        @NotBlank String adresse,
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Doit être un code couleur hexadécimal valide (#RRGGBB)")
        String couleurPrimaire,
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Doit être un code couleur hexadécimal valide (#RRGGBB)")
        String couleurSecondaire,
        String logoUrl
) {
}