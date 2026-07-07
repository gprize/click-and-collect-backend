package com.gwpriso.click_and_collect_backend.utilisateur;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/{id}")
    public UtilisateurResponse findById(@PathVariable UUID id) {
        return utilisateurService.findById(id);
    }

    @PostMapping
    public ResponseEntity<UtilisateurResponse> create(@Valid @RequestBody UtilisateurRequest request) {
        UtilisateurResponse response = utilisateurService.create(request);
        return ResponseEntity.created(URI.create("/api/utilisateurs/" + response.id())).body(response);
    }

    @GetMapping
    public UtilisateurResponse findByEmail(@RequestParam String email) {
        return utilisateurService.findByEmail(email);
    }
}