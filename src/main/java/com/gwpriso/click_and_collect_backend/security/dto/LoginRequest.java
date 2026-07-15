package com.gwpriso.click_and_collect_backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record LoginRequest(
        @NotNull UUID magasinId,
        @NotBlank String email,
        @NotBlank String motDePasse
) {
}