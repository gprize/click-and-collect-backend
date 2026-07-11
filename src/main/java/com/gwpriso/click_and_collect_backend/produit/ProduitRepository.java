package com.gwpriso.click_and_collect_backend.produit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProduitRepository extends JpaRepository<Produit, UUID> {

    List<Produit> findByMagasinId(UUID magasinId);
    List<Produit> findByMagasinIdAndNomContainingIgnoreCase(UUID magasinId, String recherche);
}