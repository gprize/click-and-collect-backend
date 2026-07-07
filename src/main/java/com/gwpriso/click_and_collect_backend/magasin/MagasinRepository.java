package com.gwpriso.click_and_collect_backend.magasin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MagasinRepository extends JpaRepository<Magasin, UUID> {
}