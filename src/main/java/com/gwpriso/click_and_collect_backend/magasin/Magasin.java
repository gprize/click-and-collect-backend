package com.gwpriso.click_and_collect_backend.magasin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "magasin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Magasin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(name = "couleur_primaire", nullable = false, length = 7)
    private String couleurPrimaire = "#3B6B3E";

    @Column(name = "couleur_secondaire", nullable = false, length = 7)
    private String couleurSecondaire = "#B23A2E";

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "cree_le", nullable = false, updatable = false)
    private LocalDateTime creeLe;

    @PrePersist
    protected void onCreate() {
        this.creeLe = LocalDateTime.now();
    }
}