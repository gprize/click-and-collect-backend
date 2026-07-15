package com.gwpriso.click_and_collect_backend.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccessGuard {

    public void verifierMagasin(AuthenticatedUser user, UUID magasinId) {
        if (!user.magasinId().equals(magasinId)) {
            throw new AccesRefuseException("Accès refusé à cette ressource");
        }
    }

    public void verifierStaff(AuthenticatedUser user) {
        if (user.type() != TypeUtilisateur.STAFF) {
            throw new AccesRefuseException("Réservé au personnel du magasin");
        }
    }

    public void verifierProprietaire(AuthenticatedUser user, UUID utilisateurId) {
        if (!user.id().equals(utilisateurId)) {
            throw new AccesRefuseException("Accès refusé à cette ressource");
        }
    }
}