package com.gwpriso.click_and_collect_backend.utilisateur;

import java.util.UUID;

public record UtilisateurResponse(
        UUID id,
        String email,
        String telephone
) {
    public static UtilisateurResponse from(Utilisateur utilisateur) {
        return new UtilisateurResponse(utilisateur.getId(), utilisateur.getEmail(), utilisateur.getTelephone());
    }
}