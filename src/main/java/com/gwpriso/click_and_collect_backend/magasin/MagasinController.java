package com.gwpriso.click_and_collect_backend.magasin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/magasins")
@RequiredArgsConstructor
public class MagasinController {

    private final MagasinService magasinService;

    @GetMapping
    public List<MagasinResponse> findAll() {
        return magasinService.findAll();
    }

    @GetMapping("/{id}")
    public MagasinResponse findById(@PathVariable UUID id) {
        return magasinService.findById(id);
    }

    @PostMapping
    public ResponseEntity<MagasinResponse> create(@Valid @RequestBody MagasinRequest request) {
        MagasinResponse response = magasinService.create(request);
        return ResponseEntity.created(URI.create("/api/magasins/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public MagasinResponse update(@PathVariable UUID id, @Valid @RequestBody MagasinRequest request) {
        return magasinService.update(id, request);
    }
}