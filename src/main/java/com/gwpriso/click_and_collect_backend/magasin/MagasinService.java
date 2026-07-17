package com.gwpriso.click_and_collect_backend.magasin;

import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MagasinService {

    private final MagasinRepository magasinRepository;

    public List<MagasinResponse> findAll() {
        return magasinRepository.findAll().stream()
                .map(MagasinResponse::from)
                .toList();
    }

    public MagasinResponse findById(UUID id) {
        return MagasinResponse.from(getMagasinOrThrow(id));
    }

    public MagasinResponse create(MagasinRequest request) {
        Magasin magasin = new Magasin();
        magasin.setNom(request.nom());
        magasin.setAdresse(request.adresse());
        return MagasinResponse.from(magasinRepository.save(magasin));
    }

    public MagasinResponse update(UUID id, MagasinRequest request) {
        Magasin magasin = getMagasinOrThrow(id);
        magasin.setNom(request.nom());
        magasin.setAdresse(request.adresse());
        if (request.couleurPrimaire() != null) magasin.setCouleurPrimaire(request.couleurPrimaire());
        if (request.couleurSecondaire() != null) magasin.setCouleurSecondaire(request.couleurSecondaire());
        magasin.setLogoUrl(request.logoUrl());
        return MagasinResponse.from(magasin);
    }

    private Magasin getMagasinOrThrow(UUID id) {
        return magasinRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Magasin introuvable : " + id));
    }
}