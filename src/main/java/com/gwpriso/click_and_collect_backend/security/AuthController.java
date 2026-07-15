package com.gwpriso.click_and_collect_backend.security;

import com.gwpriso.click_and_collect_backend.security.dto.AuthResponse;
import com.gwpriso.click_and_collect_backend.security.dto.LoginRequest;
import com.gwpriso.click_and_collect_backend.utilisateur.UtilisateurRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/client/register")
    public ResponseEntity<AuthResponse> registerClient(@Valid @RequestBody UtilisateurRequest request) {
        return ResponseEntity.ok(authService.registerClient(request));
    }

    @PostMapping("/client/login")
    public ResponseEntity<AuthResponse> loginClient(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginClient(request));
    }

    @PostMapping("/staff/login")
    public ResponseEntity<AuthResponse> loginStaff(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginStaff(request));
    }
}