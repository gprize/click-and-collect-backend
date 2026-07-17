package com.gwpriso.click_and_collect_backend.commande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommandeRepository extends JpaRepository<Commande, UUID> {

    List<Commande> findByUtilisateurId(UUID utilisateurId);

    List<Commande> findByMagasinIdAndStatut(UUID magasinId, StatutCommande statut);

    Optional<Commande> findByCodeRetrait(String codeRetrait);

    List<Commande> findByMagasinIdAndStatutIn(UUID magasinId, List<StatutCommande> statuts);

    @Query("""
    SELECT CAST(c.creeLe AS date) AS jour, COALESCE(SUM(lc.quantite * lc.prixUnitaire), 0) AS total
    FROM Commande c JOIN c.lignes lc
    WHERE c.magasin.id = :magasinId
      AND c.statut IN :statuts
      AND c.creeLe >= :depuis
    GROUP BY CAST(c.creeLe AS date)
    ORDER BY CAST(c.creeLe AS date)
    """)
    List<Object[]> sommeVentesParJour(UUID magasinId, List<StatutCommande> statuts, LocalDateTime depuis);

    @Query("""
    SELECT lc.produit.nom AS nom, SUM(lc.quantite) AS quantite
    FROM LigneCommande lc JOIN lc.commande c
    WHERE c.magasin.id = :magasinId
      AND c.statut IN :statuts
    GROUP BY lc.produit.nom
    ORDER BY SUM(lc.quantite) DESC
    """)
    List<Object[]> produitsPlusVendus(UUID magasinId, List<StatutCommande> statuts, Pageable pageable);

    @Query(value = """
    SELECT EXTRACT(DOW FROM c.cree_le)::int AS jour_semaine,
           COALESCE(SUM(lc.quantite * lc.prix_unitaire), 0) AS total
    FROM commande c
    JOIN ligne_commande lc ON lc.commande_id = c.id
    WHERE c.magasin_id = :magasinId
      AND c.statut IN (:statuts)
      AND c.cree_le >= :depuis
    GROUP BY EXTRACT(DOW FROM c.cree_le)
    """, nativeQuery = true)
    List<Object[]> sommeVentesParJourSemaine(UUID magasinId, List<String> statuts, LocalDateTime depuis);
}