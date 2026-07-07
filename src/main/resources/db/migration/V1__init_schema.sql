CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE utilisateur (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             email VARCHAR(255) NOT NULL UNIQUE,
                             telephone VARCHAR(20) NOT NULL,
                             mot_de_passe_hash VARCHAR(255) NOT NULL,
                             cree_le TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE magasin (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         nom VARCHAR(150) NOT NULL,
                         adresse VARCHAR(255) NOT NULL,
                         cree_le TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE produit (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         magasin_id UUID NOT NULL REFERENCES magasin(id),
                         nom VARCHAR(200) NOT NULL,
                         prix NUMERIC(10,2) NOT NULL CHECK (prix >= 0),
                         quantite_stock INTEGER NOT NULL DEFAULT 0 CHECK (quantite_stock >= 0),
                         cree_le TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE commande (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          utilisateur_id UUID NOT NULL REFERENCES utilisateur(id),
                          magasin_id UUID NOT NULL REFERENCES magasin(id),
                          statut VARCHAR(30) NOT NULL DEFAULT 'EN_ATTENTE_PAIEMENT',
                          code_retrait VARCHAR(6),
                          cree_le TIMESTAMP NOT NULL DEFAULT now(),
                          CONSTRAINT chk_statut CHECK (statut IN (
                                                                  'EN_ATTENTE_PAIEMENT', 'PAYEE', 'EN_PREPARATION',
                                                                  'PRETE', 'RECUPEREE', 'ANNULEE'
                              ))
);

CREATE TABLE ligne_commande (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                commande_id UUID NOT NULL REFERENCES commande(id) ON DELETE CASCADE,
                                produit_id UUID NOT NULL REFERENCES produit(id),
                                quantite INTEGER NOT NULL CHECK (quantite > 0),
                                prix_unitaire NUMERIC(10,2) NOT NULL CHECK (prix_unitaire >= 0)
);

CREATE TABLE paiement (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          commande_id UUID NOT NULL UNIQUE REFERENCES commande(id),
                          operateur VARCHAR(20) NOT NULL CHECK (operateur IN ('MTN_MOMO', 'ORANGE_MONEY')),
                          statut VARCHAR(20) NOT NULL DEFAULT 'INITIE',
                          reference_externe VARCHAR(100),
                          cree_le TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_produit_magasin ON produit(magasin_id);
CREATE INDEX idx_commande_utilisateur ON commande(utilisateur_id);
CREATE INDEX idx_commande_magasin ON commande(magasin_id);
CREATE INDEX idx_ligne_commande_commande ON ligne_commande(commande_id);