package com.gwpriso.click_and_collect_backend.security;

import com.gwpriso.click_and_collect_backend.security.dto.AuthResponse;
import com.gwpriso.click_and_collect_backend.security.dto.LoginRequest;
import com.gwpriso.click_and_collect_backend.staff.UtilisateurStaff;
import com.gwpriso.click_and_collect_backend.staff.UtilisateurStaffRepository;
import com.gwpriso.click_and_collect_backend.utilisateur.Utilisateur;
import com.gwpriso.click_and_collect_backend.utilisateur.UtilisateurRepository;
import com.gwpriso.click_and_collect_backend.utilisateur.UtilisateurRequest;
import com.gwpriso.click_and_collect_backend.utilisateur.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final String ROLE_CLIENT = "CLIENT";

    private final UtilisateurService utilisateurService;
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurStaffRepository utilisateurStaffRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registerClient(UtilisateurRequest request) {
        Utilisateur utilisateur = utilisateurService.create(request);
        String token = jwtService.genererToken(
                utilisateur.getId(), utilisateur.getMagasin().getId(), ROLE_CLIENT, TypeUtilisateur.CLIENT);
        return new AuthResponse(token, utilisateur.getId(), utilisateur.getMagasin().getId(), ROLE_CLIENT);
    }

    public AuthResponse loginClient(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByEmailAndMagasinId(request.email(), request.magasinId())
                .orElseThrow(() -> new AuthenticationException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.motDePasse(), utilisateur.getMotDePasseHash())) {
            throw new AuthenticationException("Email ou mot de passe incorrect");
        }

        String token = jwtService.genererToken(
                utilisateur.getId(), utilisateur.getMagasin().getId(), ROLE_CLIENT, TypeUtilisateur.CLIENT);
        return new AuthResponse(token, utilisateur.getId(), utilisateur.getMagasin().getId(), ROLE_CLIENT);
    }

    public AuthResponse loginStaff(LoginRequest request) {
        UtilisateurStaff staff = utilisateurStaffRepository.findByEmailAndMagasinId(request.email(), request.magasinId())
                .orElseThrow(() -> new AuthenticationException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.motDePasse(), staff.getMotDePasseHash())) {
            throw new AuthenticationException("Email ou mot de passe incorrect");
        }

        String role = staff.getRole().name();
        String token = jwtService.genererToken(staff.getId(), staff.getMagasin().getId(), role, TypeUtilisateur.STAFF);
        return new AuthResponse(token, staff.getId(), staff.getMagasin().getId(), role);
    }
}