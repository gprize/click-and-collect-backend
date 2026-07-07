package com.gwpriso.click_and_collect_backend.commande;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande, UUID> {
}