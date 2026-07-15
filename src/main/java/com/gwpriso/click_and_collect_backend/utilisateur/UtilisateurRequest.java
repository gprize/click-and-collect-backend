package com.gwpriso.click_and_collect_backend.utilisateur;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UtilisateurRequest(
        @NotNull UUID magasinId,
        @NotBlank @Email String email,
        @NotBlank String telephone,
        @NotBlank @Size(min = 8) String motDePasse
) {
}