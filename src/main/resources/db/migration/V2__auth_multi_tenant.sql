-- Rattache chaque utilisateur client à son magasin (tenant)
ALTER TABLE utilisateur ADD COLUMN magasin_id UUID REFERENCES magasin(id);

-- Pas de vraies données en production à ce stade : on repart propre plutôt que de backfiller du faux
DELETE FROM utilisateur;

ALTER TABLE utilisateur ALTER COLUMN magasin_id SET NOT NULL;

-- L'email n'est plus unique globalement, seulement par magasin
ALTER TABLE utilisateur DROP CONSTRAINT utilisateur_email_key;
ALTER TABLE utilisateur ADD CONSTRAINT uq_utilisateur_email_magasin UNIQUE (email, magasin_id);

CREATE INDEX idx_utilisateur_magasin ON utilisateur(magasin_id);

-- Comptes staff (app dashboard) : distincts des comptes client
CREATE TABLE utilisateur_staff (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   magasin_id UUID NOT NULL REFERENCES magasin(id),
                                   email VARCHAR(255) NOT NULL,
                                   mot_de_passe_hash VARCHAR(255) NOT NULL,
                                   role VARCHAR(20) NOT NULL,
                                   cree_le TIMESTAMP NOT NULL DEFAULT now(),
                                   CONSTRAINT chk_role_staff CHECK (role IN ('ADMIN_MAGASIN', 'EMPLOYE')),
                                   CONSTRAINT uq_staff_email_magasin UNIQUE (email, magasin_id)
);

CREATE INDEX idx_staff_magasin ON utilisateur_staff(magasin_id);