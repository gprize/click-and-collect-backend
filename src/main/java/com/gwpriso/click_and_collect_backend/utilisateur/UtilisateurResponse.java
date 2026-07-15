package com.gwpriso.click_and_collect_backend.utilisateur;

import java.util.UUID;

public record UtilisateurResponse(
        UUID id,
        UUID magasinId,
        String email,
        String telephone
) {
    public static UtilisateurResponse from(Utilisateur utilisateur) {
        return new UtilisateurResponse(
                utilisateur.getId(),
                utilisateur.getMagasin().getId(),
                utilisateur.getEmail(),
                utilisateur.getTelephone()
        );
    }
}