package com.gwpriso.click_and_collect_backend.security.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        UUID utilisateurId,
        UUID magasinId,
        String role
) {
}