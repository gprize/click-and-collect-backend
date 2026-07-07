package com.gwpriso.click_and_collect_backend.commande;

import com.gwpriso.click_and_collect_backend.common.exception.EntityNotFoundException;
import com.gwpriso.click_and_collect_backend.common.exception.StockInsuffisantException;
import com.gwpriso.click_and_collect_backend.magasin.Magasin;
import com.gwpriso.click_and_collect_backend.magasin.MagasinRepository;
import com.gwpriso.click_and_collect_backend.produit.Produit;
import com.gwpriso.click_and_collect_backend.produit.ProduitRepository;
import com.gwpriso.click_and_collect_backend.utilisateur.Utilisateur;
import com.gwpriso.click_and_collect_backend.utilisateur.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandeService {

    private static final String CODE_RETRAIT_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_RETRAIT_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CommandeRepository commandeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final MagasinRepository magasinRepository;
    private final ProduitRepository produitRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public CommandeResponse create(CommandeRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(request.utilisateurId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + request.utilisateurId()));

        Magasin magasin = magasinRepository.findById(request.magasinId())
                .orElseThrow(() -> new EntityNotFoundException("Magasin introuvable : " + request.magasinId()));

        Commande commande = new Commande();
        commande.setUtilisateur(utilisateur);
        commande.setMagasin(magasin);

        for (CommandeRequest.LigneRequest ligneRequest : request.lignes()) {
            Produit produit = produitRepository.findById(ligneRequest.produitId())
                    .orElseThrow(() -> new EntityNotFoundException("Produit introuvable : " + ligneRequest.produitId()));

            if (!produit.getMagasin().getId().equals(magasin.getId())) {
                throw new IllegalArgumentException(
                        "Le produit %s n'appartient pas au magasin %s".formatted(produit.getId(), magasin.getId()));
            }

            LigneCommande ligne = new LigneCommande();
            ligne.setCommande(commande);
            ligne.setProduit(produit);
            ligne.setQuantite(ligneRequest.quantite());
            ligne.setPrixUnitaire(produit.getPrix());

            commande.getLignes().add(ligne);
        }

        return CommandeResponse.from(commandeRepository.save(commande));
    }

    public CommandeResponse findById(UUID id) {
        return CommandeResponse.from(getCommandeOrThrow(id));
    }

    // Temporaire : à remplacer par le vrai flux déclenché par le callback MTN MoMo / Orange Money.
    public CommandeResponse marquerPayee(UUID id) {
        Commande commande = getCommandeOrThrow(id);

        if (commande.getStatut() != StatutCommande.EN_ATTENTE_PAIEMENT) {
            throw new IllegalStateException(
                    "Impossible de marquer payée : statut actuel = " + commande.getStatut());
        }

        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();
            if (produit.getQuantiteStock() < ligne.getQuantite()) {
                throw new StockInsuffisantException(
                        "Stock insuffisant pour %s (demandé : %d, disponible : %d)"
                                .formatted(produit.getNom(), ligne.getQuantite(), produit.getQuantiteStock()));
            }
        }

        for (LigneCommande ligne : commande.getLignes()) {
            Produit produit = ligne.getProduit();
            produit.setQuantiteStock(produit.getQuantiteStock() - ligne.getQuantite());
        }

        commande.setStatut(StatutCommande.PAYEE);
        commande.setCodeRetrait(genererCodeRetrait());

        CommandeResponse response = CommandeResponse.from(commande);
        messagingTemplate.convertAndSend("/topic/magasins/" + commande.getMagasin().getId() + "/commandes", response);
        return response;
    }

    private Commande getCommandeOrThrow(UUID id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande introuvable : " + id));
    }

    private String genererCodeRetrait() {
        StringBuilder sb = new StringBuilder(CODE_RETRAIT_LENGTH);
        for (int i = 0; i < CODE_RETRAIT_LENGTH; i++) {
            sb.append(CODE_RETRAIT_CHARS.charAt(RANDOM.nextInt(CODE_RETRAIT_CHARS.length())));
        }
        return sb.toString();
    }

    public List<CommandeResponse> findByMagasin(UUID magasinId, List<StatutCommande> statuts) {
        return commandeRepository.findByMagasinIdAndStatutIn(magasinId, statuts).stream()
                .map(CommandeResponse::from)
                .toList();
    }

    public CommandeResponse demarrerPreparation(UUID id) {
        Commande commande = getCommandeOrThrow(id);
        if (commande.getStatut() != StatutCommande.PAYEE) {
            throw new IllegalStateException(
                    "Impossible de démarrer la préparation : statut actuel = " + commande.getStatut());
        }
        commande.setStatut(StatutCommande.EN_PREPARATION);
        return CommandeResponse.from(commande);
    }

    public CommandeResponse marquerPrete(UUID id) {
        Commande commande = getCommandeOrThrow(id);
        if (commande.getStatut() != StatutCommande.EN_PREPARATION) {
            throw new IllegalStateException(
                    "Impossible de marquer prête : statut actuel = " + commande.getStatut());
        }
        commande.setStatut(StatutCommande.PRETE);
        CommandeResponse response = CommandeResponse.from(commande);
        messagingTemplate.convertAndSend("/topic/commandes/" + commande.getId(), response);
        return response;
    }

    public CommandeResponse validerRecuperation(UUID magasinId, String codeRetrait) {
        Commande commande = commandeRepository.findByCodeRetrait(codeRetrait)
                .orElseThrow(() -> new EntityNotFoundException("Aucune commande avec ce code : " + codeRetrait));

        // Volontaire : même message que "code inexistant" si le code appartient à un autre magasin.
        // Évite de révéler qu'un code valide existe ailleurs.
        if (!commande.getMagasin().getId().equals(magasinId)) {
            throw new EntityNotFoundException("Aucune commande avec ce code : " + codeRetrait);
        }
        if (commande.getStatut() != StatutCommande.PRETE) {
            throw new IllegalStateException(
                    "Commande non prête pour retrait : statut actuel = " + commande.getStatut());
        }

        commande.setStatut(StatutCommande.RECUPEREE);
        return CommandeResponse.from(commande);
    }
}