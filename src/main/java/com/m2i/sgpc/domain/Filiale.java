package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Filiale.
 */
@Entity
@Table(name = "filiale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Filiale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "sigle")
    private String sigle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filiale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "productions", "stockages", "colisages", "emails", "filiale", "user", "producteurs", "receveurs" },
        allowSetters = true
    )
    private Set<Personne> personnes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Filiale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return this.denomination;
    }

    public Filiale denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getSigle() {
        return this.sigle;
    }

    public Filiale sigle(String sigle) {
        this.setSigle(sigle);
        return this;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public Set<Personne> getPersonnes() {
        return this.personnes;
    }

    public void setPersonnes(Set<Personne> personnes) {
        if (this.personnes != null) {
            this.personnes.forEach(i -> i.setFiliale(null));
        }
        if (personnes != null) {
            personnes.forEach(i -> i.setFiliale(this));
        }
        this.personnes = personnes;
    }

    public Filiale personnes(Set<Personne> personnes) {
        this.setPersonnes(personnes);
        return this;
    }

    public Filiale addPersonne(Personne personne) {
        this.personnes.add(personne);
        personne.setFiliale(this);
        return this;
    }

    public Filiale removePersonne(Personne personne) {
        this.personnes.remove(personne);
        personne.setFiliale(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Filiale)) {
            return false;
        }
        return getId() != null && getId().equals(((Filiale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Filiale{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", sigle='" + getSigle() + "'" +
            "}";
    }
}
