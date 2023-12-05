package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stockage.
 */
@Entity
@Table(name = "stockage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stockage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "code")
    private String code;

    @Column(name = "quantite")
    private String quantite;

    @Column(name = "modifier_par")
    private String modifierPar;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

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

    public Stockage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return this.denomination;
    }

    public Stockage denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getCode() {
        return this.code;
    }

    public Stockage code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantite() {
        return this.quantite;
    }

    public Stockage quantite(String quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getModifierPar() {
        return this.modifierPar;
    }

    public Stockage modifierPar(String modifierPar) {
        this.setModifierPar(modifierPar);
        return this;
    }

    public void setModifierPar(String modifierPar) {
        this.modifierPar = modifierPar;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Stockage dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Stockage personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stockage)) {
            return false;
        }
        return getId() != null && getId().equals(((Stockage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stockage{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", code='" + getCode() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", modifierPar='" + getModifierPar() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
