package com.gwpriso.click_and_collect_backend.utilisateur;

import com.gwpriso.click_and_collect_backend.common.exception.EmailDejaUtiliseException;
import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurResponse findById(UUID id) {
        return UtilisateurResponse.from(getUtilisateurOrThrow(id));
    }

    public UtilisateurResponse create(UtilisateurRequest request) {
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new EmailDejaUtiliseException("Email déjà utilisé : " + request.email());
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.email());
        utilisateur.setTelephone(request.telephone());
        // TODO : remplacer par BCryptPasswordEncoder.encode() dès que Spring Security est ajouté
        utilisateur.setMotDePasseHash(request.motDePasse());

        return UtilisateurResponse.from(utilisateurRepository.save(utilisateur));
    }

    private Utilisateur getUtilisateurOrThrow(UUID id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + id));
    }

    public UtilisateurResponse findByEmail(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + email));
        return UtilisateurResponse.from(utilisateur);
    }
}