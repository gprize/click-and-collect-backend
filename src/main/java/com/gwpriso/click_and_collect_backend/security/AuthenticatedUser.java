package com.gwpriso.click_and_collect_backend.security;

import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        UUID magasinId,
        String role,
        TypeUtilisateur type
) {
}