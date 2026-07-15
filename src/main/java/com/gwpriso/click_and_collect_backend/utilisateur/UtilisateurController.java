package com.gwpriso.click_and_collect_backend.utilisateur;

import com.gwpriso.click_and_collect_backend.security.AccessGuard;
import com.gwpriso.click_and_collect_backend.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final AccessGuard accessGuard;

    @GetMapping("/{id}")
    public UtilisateurResponse findById(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        accessGuard.verifierProprietaire(user, id);
        return utilisateurService.findById(id);
    }
}