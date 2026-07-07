package com.gwpriso.click_and_collect_backend.magasin;

import java.util.UUID;

public record MagasinResponse(
        UUID id,
        String nom,
        String adresse
) {
    public static MagasinResponse from(Magasin magasin) {
        return new MagasinResponse(magasin.getId(), magasin.getNom(), magasin.getAdresse());
    }
}