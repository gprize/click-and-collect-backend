package com.gwpriso.click_and_collect_backend.staff;

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
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    private final AccessGuard accessGuard;

    @GetMapping
    public List<StaffResponse> findByMagasin(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId) {
        accessGuard.verifierAdminMagasin(user);
        accessGuard.verifierMagasin(user, magasinId);
        return staffService.findByMagasin(magasinId);
    }

    @PostMapping
    public ResponseEntity<StaffResponse> create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @Valid @RequestBody StaffRequest request) {
        accessGuard.verifierAdminMagasin(user);
        accessGuard.verifierMagasin(user, magasinId);
        StaffResponse response = staffService.create(magasinId, request);
        return ResponseEntity.created(URI.create("/api/staff/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam UUID magasinId,
            @PathVariable UUID id) {
        accessGuard.verifierAdminMagasin(user);
        accessGuard.verifierMagasin(user, magasinId);
        staffService.delete(magasinId, id);
        return ResponseEntity.noContent().build();
    }
}