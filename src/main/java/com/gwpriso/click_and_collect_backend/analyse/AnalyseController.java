package com.gwpriso.click_and_collect_backend.analyse;

import com.gwpriso.click_and_collect_backend.security.AccessGuard;
import com.gwpriso.click_and_collect_backend.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analyses")
@RequiredArgsConstructor
public class AnalyseController {

    private final AnalyseService analyseService;
    private final AccessGuard accessGuard;

    @GetMapping("/ventes")
    public List<VenteParJourResponse> ventesParJour(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @RequestParam(defaultValue = "14") int jours) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, magasinId);
        return analyseService.ventesParJour(magasinId, jours);
    }

    @GetMapping("/produits-populaires")
    public List<ProduitVenduResponse> produitsPlusVendus(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @RequestParam(defaultValue = "5") int limite) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, magasinId);
        return analyseService.produitsPlusVendus(magasinId, limite);
    }

    @GetMapping("/ventes-par-jour-semaine")
    public List<JourSemaineVenteResponse> ventesParJourSemaine(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @RequestParam(defaultValue = "90") int jours) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, magasinId);
        return analyseService.ventesParJourSemaine(magasinId, jours);
    }
}