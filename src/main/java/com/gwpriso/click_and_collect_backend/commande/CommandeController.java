package com.gwpriso.click_and_collect_backend.commande;

import com.gwpriso.click_and_collect_backend.security.AccessGuard;
import com.gwpriso.click_and_collect_backend.security.AuthenticatedUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;
    private final AccessGuard accessGuard;

    @PostMapping
    public ResponseEntity<CommandeResponse> create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CommandeRequest request) {
        accessGuard.verifierMagasin(user, request.magasinId());
        accessGuard.verifierProprietaire(user, request.utilisateurId());
        CommandeResponse response = commandeService.create(request);
        return ResponseEntity.created(URI.create("/api/commandes/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public CommandeResponse findById(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        CommandeResponse commande = commandeService.findById(id);
        accessGuard.verifierMagasin(user, commande.magasinId());
        return commande;
    }

    @GetMapping
    public List<CommandeResponse> findByMagasin(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @RequestParam(required = false) List<StatutCommande> statuts) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, magasinId);
        List<StatutCommande> filtres = (statuts != null && !statuts.isEmpty())
                ? statuts
                : List.of(StatutCommande.PAYEE, StatutCommande.EN_PREPARATION, StatutCommande.PRETE);
        return commandeService.findByMagasin(magasinId, filtres);
    }

    @PatchMapping("/{id}/marquer-payee")
    public CommandeResponse marquerPayee(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        CommandeResponse commande = commandeService.findById(id);
        accessGuard.verifierMagasin(user, commande.magasinId());
        return commandeService.marquerPayee(id);
    }

    @PatchMapping("/{id}/demarrer-preparation")
    public CommandeResponse demarrerPreparation(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        accessGuard.verifierStaff(user);
        CommandeResponse commande = commandeService.findById(id);
        accessGuard.verifierMagasin(user, commande.magasinId());
        return commandeService.demarrerPreparation(id);
    }

    @PatchMapping("/{id}/marquer-prete")
    public CommandeResponse marquerPrete(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        accessGuard.verifierStaff(user);
        CommandeResponse commande = commandeService.findById(id);
        accessGuard.verifierMagasin(user, commande.magasinId());
        return commandeService.marquerPrete(id);
    }

    @PostMapping("/recuperation")
    public CommandeResponse validerRecuperation(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody RecuperationRequest request) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, request.magasinId());
        return commandeService.validerRecuperation(request.magasinId(), request.codeRetrait());
    }

    public record RecuperationRequest(
            @NotNull UUID magasinId,
            @NotBlank String codeRetrait
    ) {
    }
}