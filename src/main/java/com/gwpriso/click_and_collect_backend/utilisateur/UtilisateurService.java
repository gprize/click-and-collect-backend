package com.gwpriso.click_and_collect_backend.utilisateur;

import com.gwpriso.click_and_collect_backend.common.exception.EmailDejaUtiliseException;
import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import com.gwpriso.click_and_collect_backend.magasin.Magasin;
import com.gwpriso.click_and_collect_backend.magasin.MagasinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final MagasinRepository magasinRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurResponse findById(UUID id) {
        return UtilisateurResponse.from(getUtilisateurOrThrow(id));
    }

    public Utilisateur create(UtilisateurRequest request) {
        if (utilisateurRepository.existsByEmailAndMagasinId(request.email(), request.magasinId())) {
            throw new EmailDejaUtiliseException("Email déjà utilisé pour ce magasin : " + request.email());
        }

        Magasin magasin = magasinRepository.findById(request.magasinId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin introuvable : " + request.magasinId()));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setMagasin(magasin);
        utilisateur.setEmail(request.email());
        utilisateur.setTelephone(request.telephone());
        utilisateur.setMotDePasseHash(passwordEncoder.encode(request.motDePasse()));

        return utilisateurRepository.save(utilisateur);
    }

    private Utilisateur getUtilisateurOrThrow(UUID id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + id));
    }
}