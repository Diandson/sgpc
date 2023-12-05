package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Email.
 */
@Entity
@Table(name = "email")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "objet")
    private String objet;

    @Column(name = "contenu")
    private String contenu;

    @Column(name = "destinataire")
    private String destinataire;

    @Column(name = "date_envoi")
    private ZonedDateTime dateEnvoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "emails", "personne" }, allowSetters = true)
    private Colisage colisage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "productions", "stockages", "colisages", "emails", "filiale", "user", "producteurs", "receveurs" },
        allowSetters = true
    )
    private Personne personne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Email id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return this.objet;
    }

    public Email objet(String objet) {
        this.setObjet(objet);
        return this;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Email contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDestinataire() {
        return this.destinataire;
    }

    public Email destinataire(String destinataire) {
        this.setDestinataire(destinataire);
        return this;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public ZonedDateTime getDateEnvoi() {
        return this.dateEnvoi;
    }

    public Email dateEnvoi(ZonedDateTime dateEnvoi) {
        this.setDateEnvoi(dateEnvoi);
        return this;
    }

    public void setDateEnvoi(ZonedDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Colisage getColisage() {
        return this.colisage;
    }

    public void setColisage(Colisage colisage) {
        this.colisage = colisage;
    }

    public Email colisage(Colisage colisage) {
        this.setColisage(colisage);
        return this;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Email personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        return getId() != null && getId().equals(((Email) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Email{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", destinataire='" + getDestinataire() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            "}";
    }
}
