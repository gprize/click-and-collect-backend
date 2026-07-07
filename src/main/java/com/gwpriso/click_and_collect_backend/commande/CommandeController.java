package com.gwpriso.click_and_collect_backend.commande;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeResponse> create(@Valid @RequestBody CommandeRequest request) {
        CommandeResponse response = commandeService.create(request);
        return ResponseEntity.created(URI.create("/api/commandes/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public CommandeResponse findById(@PathVariable UUID id) {
        return commandeService.findById(id);
    }

    // Endpoint temporaire pour tester le flux sans paiement réel
    @PatchMapping("/{id}/marquer-payee")
    public CommandeResponse marquerPayee(@PathVariable UUID id) {
        return commandeService.marquerPayee(id);
    }

    @GetMapping
    public List<CommandeResponse> findByMagasin(
            @RequestParam UUID magasinId,
            @RequestParam(required = false) List<StatutCommande> statuts) {
        List<StatutCommande> filtres = (statuts != null && !statuts.isEmpty())
                ? statuts
                : List.of(StatutCommande.PAYEE, StatutCommande.EN_PREPARATION, StatutCommande.PRETE);
        return commandeService.findByMagasin(magasinId, filtres);
    }

    @PatchMapping("/{id}/demarrer-preparation")
    public CommandeResponse demarrerPreparation(@PathVariable UUID id) {
        return commandeService.demarrerPreparation(id);
    }

    @PatchMapping("/{id}/marquer-prete")
    public CommandeResponse marquerPrete(@PathVariable UUID id) {
        return commandeService.marquerPrete(id);
    }

    @PostMapping("/recuperation")
    public CommandeResponse validerRecuperation(@Valid @RequestBody RecuperationRequest request) {
        return commandeService.validerRecuperation(request.magasinId(), request.codeRetrait());
    }

    public record RecuperationRequest(
            @NotNull UUID magasinId,
            @NotBlank String codeRetrait
    ) {
    }
}