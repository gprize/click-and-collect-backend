package com.gwpriso.click_and_collect_backend.config;

import com.gwpriso.click_and_collect_backend.magasin.Magasin;
import com.gwpriso.click_and_collect_backend.magasin.MagasinRepository;
import com.gwpriso.click_and_collect_backend.produit.Produit;
import com.gwpriso.click_and_collect_backend.produit.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile({"local", "demo"})
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final MagasinRepository magasinRepository;
    private final ProduitRepository produitRepository;

    @Override
    public void run(String... args) {
        if (magasinRepository.count() > 0) {
            log.info("Données de test déjà présentes, seed ignoré.");
            return;
        }

        log.info("Base vide détectée — injection des données de démo.");

        Magasin dovv = creerMagasin("Dovv Akwa", "Rue Joss, Akwa, Douala");
        creerProduit(dovv, "Riz Uncle Sam 5kg", "4500", 25);
        creerProduit(dovv, "Huile Mayor 1L", "1500", 30);
        creerProduit(dovv, "Sucre en poudre 1kg", "900", 40);
        creerProduit(dovv, "Savon Marseille", "500", 50);
        creerProduit(dovv, "Lait Nido 900g", "6200", 15);
        creerProduit(dovv, "Farine de blé 1kg", "800", 35);

        Magasin superU = creerMagasin("Super U Bonanjo", "Avenue de Gaulle, Bonanjo, Douala");
        creerProduit(superU, "Pâtes alimentaires 500g", "600", 45);
        creerProduit(superU, "Tomate concentrée 400g", "700", 38);
        creerProduit(superU, "Boisson Top Ananas 1.5L", "1000", 28);
        creerProduit(superU, "Œufs (plateau de 30)", "3200", 12);
        creerProduit(superU, "Poulet congelé 1kg", "3500", 10);

        Magasin carrefour = creerMagasin("Carrefour Bonapriso", "Rue Njo-Njo, Bonapriso, Douala");
        creerProduit(carrefour, "Café Nescafé 200g", "3800", 18);
        creerProduit(carrefour, "Yaourt nature (pack de 4)", "1800", 22);
        creerProduit(carrefour, "Jus de fruit 1L", "1200", 25);
        creerProduit(carrefour, "Papier hygiénique (pack de 6)", "1500", 30);
        creerProduit(carrefour, "Biscuits assortiment", "900", 33);

        log.info("Seed terminé : 3 magasins, {} produits créés.", produitRepository.count());
    }

    private Magasin creerMagasin(String nom, String adresse) {
        Magasin magasin = new Magasin();
        magasin.setNom(nom);
        magasin.setAdresse(adresse);
        return magasinRepository.save(magasin);
    }

    private void creerProduit(Magasin magasin, String nom, String prix, int stock) {
        Produit produit = new Produit();
        produit.setMagasin(magasin);
        produit.setNom(nom);
        produit.setPrix(new BigDecimal(prix));
        produit.setQuantiteStock(stock);
        produitRepository.save(produit);
    }
}