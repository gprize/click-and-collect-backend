package com.gwpriso.click_and_collect_backend.commande;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommandeRepository extends JpaRepository<Commande, UUID> {

    List<Commande> findByUtilisateurId(UUID utilisateurId);

    List<Commande> findByMagasinIdAndStatut(UUID magasinId, StatutCommande statut);

    Optional<Commande> findByCodeRetrait(String codeRetrait);

    List<Commande> findByMagasinIdAndStatutIn(UUID magasinId, List<StatutCommande> statuts);
}