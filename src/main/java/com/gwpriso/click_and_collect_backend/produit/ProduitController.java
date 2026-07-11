package com.gwpriso.click_and_collect_backend.produit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    public List<ProduitResponse> findByMagasin(
            @RequestParam UUID magasinId,
            @RequestParam(required = false) String recherche) {
        return produitService.findByMagasin(magasinId, recherche);
    }

    @GetMapping("/{id}")
    public ProduitResponse findById(@PathVariable UUID id) {
        return produitService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ProduitResponse> create(@Valid @RequestBody ProduitRequest request) {
        ProduitResponse response = produitService.create(request);
        return ResponseEntity.created(URI.create("/api/produits/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ProduitResponse update(@PathVariable UUID id, @Valid @RequestBody ProduitRequest request) {
        return produitService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}