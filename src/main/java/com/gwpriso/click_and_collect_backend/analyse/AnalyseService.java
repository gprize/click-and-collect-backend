package com.gwpriso.click_and_collect_backend.analyse;

import com.gwpriso.click_and_collect_backend.commande.CommandeRepository;
import com.gwpriso.click_and_collect_backend.commande.StatutCommande;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyseService {

    private static final List<StatutCommande> STATUTS_PAYES = List.of(
            StatutCommande.PAYEE, StatutCommande.EN_PREPARATION, StatutCommande.PRETE, StatutCommande.RECUPEREE
    );

    private final CommandeRepository commandeRepository;

    public List<VenteParJourResponse> ventesParJour(UUID magasinId, int jours) {
        LocalDateTime depuis = LocalDate.now().minusDays(jours - 1L).atStartOfDay();

        return commandeRepository.sommeVentesParJour(magasinId, STATUTS_PAYES, depuis).stream()
                .map(ligne -> new VenteParJourResponse(
                        ((java.sql.Date) ligne[0]).toLocalDate(),
                        (BigDecimal) ligne[1]
                ))
                .toList();
    }

    public List<ProduitVenduResponse> produitsPlusVendus(UUID magasinId, int limite) {
        return commandeRepository.produitsPlusVendus(magasinId, STATUTS_PAYES, PageRequest.of(0, limite)).stream()
                .map(ligne -> new ProduitVenduResponse((String) ligne[0], (Long) ligne[1]))
                .toList();
    }

    public List<JourSemaineVenteResponse> ventesParJourSemaine(UUID magasinId, int jours) {
        LocalDateTime depuis = LocalDate.now().minusDays(jours - 1L).atStartOfDay();
        List<String> statuts = STATUTS_PAYES.stream().map(Enum::name).toList();

        Map<Integer, BigDecimal> totalParJour = commandeRepository
                .sommeVentesParJourSemaine(magasinId, statuts, depuis).stream()
                .collect(Collectors.toMap(
                        ligne -> ((Number) ligne[0]).intValue(),
                        ligne -> (BigDecimal) ligne[1]
                ));

        // Index Postgres DOW : 0 = dimanche, 1 = lundi ... 6 = samedi
        String[] labels = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        int[] ordreAffichage = {1, 2, 3, 4, 5, 6, 0}; // affichage lundi → dimanche

        return Arrays.stream(ordreAffichage)
                .mapToObj(dow -> new JourSemaineVenteResponse(labels[dow], totalParJour.getOrDefault(dow, BigDecimal.ZERO)))
                .toList();
    }
}