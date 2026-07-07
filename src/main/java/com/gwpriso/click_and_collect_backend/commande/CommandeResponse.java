package com.gwpriso.click_and_collect_backend.commande;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CommandeResponse(
        UUID id,
        UUID utilisateurId,
        UUID magasinId,
        StatutCommande statut,
        String codeRetrait,
        BigDecimal total,
        List<LigneResponse> lignes
) {
    public record LigneResponse(
            UUID produitId,
            String produitNom,
            Integer quantite,
            BigDecimal prixUnitaire,
            BigDecimal sousTotal
    ) {
    }

    public static CommandeResponse from(Commande commande) {
        List<LigneResponse> lignes = commande.getLignes().stream()
                .map(l -> new LigneResponse(
                        l.getProduit().getId(),
                        l.getProduit().getNom(),
                        l.getQuantite(),
                        l.getPrixUnitaire(),
                        l.getPrixUnitaire().multiply(BigDecimal.valueOf(l.getQuantite()))
                ))
                .toList();

        BigDecimal total = lignes.stream()
                .map(LigneResponse::sousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CommandeResponse(
                commande.getId(),
                commande.getUtilisateur().getId(),
                commande.getMagasin().getId(),
                commande.getStatut(),
                commande.getCodeRetrait(),
                total,
                lignes
        );
    }
}