package com.gwpriso.click_and_collect_backend.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmailAndMagasinId(String email, UUID magasinId);

    boolean existsByEmailAndMagasinId(String email, UUID magasinId);
}