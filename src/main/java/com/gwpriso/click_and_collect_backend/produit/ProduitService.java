package com.gwpriso.click_and_collect_backend.produit;

import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import com.gwpriso.click_and_collect_backend.magasin.Magasin;
import com.gwpriso.click_and_collect_backend.magasin.MagasinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final MagasinRepository magasinRepository;

    public List<ProduitResponse> findByMagasin(UUID magasinId) {
        return produitRepository.findByMagasinId(magasinId).stream()
                .map(ProduitResponse::from)
                .toList();
    }

    public ProduitResponse findById(UUID id) {
        Produit produit = getProduitOrThrow(id);
        return ProduitResponse.from(produit);
    }

    public ProduitResponse create(ProduitRequest request) {
        Magasin magasin = magasinRepository.findById(request.magasinId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin introuvable : " + request.magasinId()));

        Produit produit = new Produit();
        produit.setMagasin(magasin);
        produit.setNom(request.nom());
        produit.setPrix(request.prix());
        produit.setQuantiteStock(request.quantiteStock());

        return ProduitResponse.from(produitRepository.save(produit));
    }

    public ProduitResponse update(UUID id, ProduitRequest request) {
        Produit produit = getProduitOrThrow(id);
        produit.setNom(request.nom());
        produit.setPrix(request.prix());
        produit.setQuantiteStock(request.quantiteStock());
        return ProduitResponse.from(produit);
    }

    public void delete(UUID id) {
        if (!produitRepository.existsById(id)) {
            throw new EntityNotFoundException("Produit introuvable : " + id);
        }
        produitRepository.deleteById(id);
    }

    private Produit getProduitOrThrow(UUID id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produit introuvable : " + id));
    }

    public List<ProduitResponse> findByMagasin(UUID magasinId, String recherche) {
        List<Produit> produits = (recherche != null && !recherche.isBlank())
                ? produitRepository.findByMagasinIdAndNomContainingIgnoreCase(magasinId, recherche.trim())
                : produitRepository.findByMagasinId(magasinId);

        return produits.stream()
                .map(ProduitResponse::from)
                .toList();
    }
}