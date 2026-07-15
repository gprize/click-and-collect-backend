package com.gwpriso.click_and_collect_backend.staff;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurStaffRepository extends JpaRepository<UtilisateurStaff, UUID> {

    Optional<UtilisateurStaff> findByEmailAndMagasinId(String email, UUID magasinId);
}