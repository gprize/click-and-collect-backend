package com.gwpriso.click_and_collect_backend.produit;

import com.gwpriso.click_and_collect_backend.security.AccessGuard;
import com.gwpriso.click_and_collect_backend.security.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;
    private final AccessGuard accessGuard;

    @GetMapping
    public List<ProduitResponse> findByMagasin(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @RequestParam(required = false) String recherche) {
        accessGuard.verifierMagasin(user, magasinId);
        return produitService.findByMagasin(magasinId, recherche);
    }

    @GetMapping("/{id}")
    public ProduitResponse findById(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        ProduitResponse produit = produitService.findById(id);
        accessGuard.verifierMagasin(user, produit.magasinId());
        return produit;
    }

    @PostMapping
    public ResponseEntity<ProduitResponse> create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody ProduitRequest request) {
        accessGuard.verifierStaff(user);
        accessGuard.verifierMagasin(user, request.magasinId());
        ProduitResponse response = produitService.create(request);
        return ResponseEntity.created(URI.create("/api/produits/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ProduitResponse update(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id,
            @Valid @RequestBody ProduitRequest request) {
        accessGuard.verifierStaff(user);
        ProduitResponse existant = produitService.findById(id);
        accessGuard.verifierMagasin(user, existant.magasinId());
        return produitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal AuthenticatedUser user, @PathVariable UUID id) {
        accessGuard.verifierStaff(user);
        ProduitResponse existant = produitService.findById(id);
        accessGuard.verifierMagasin(user, existant.magasinId());
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}