package com.gwpriso.click_and_collect_backend.paiement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaiementRepository extends JpaRepository<Paiement, UUID> {

    Optional<Paiement> findByCommandeId(UUID commandeId);
}