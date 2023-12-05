package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Colisage.
 */
@Entity
@Table(name = "colisage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Colisage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "destination")
    private String destination;

    @Column(name = "canal")
    private String canal;

    @Column(name = "recu_par")
    private String recuPar;

    @Column(name = "est_recu")
    private Boolean estRecu;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "colisage")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "colisage", "personne" }, allowSetters = true)
    private Set<Email> emails = new HashSet<>();

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

    public Colisage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return this.destination;
    }

    public Colisage destination(String destination) {
        this.setDestination(destination);
        return this;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCanal() {
        return this.canal;
    }

    public Colisage canal(String canal) {
        this.setCanal(canal);
        return this;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getRecuPar() {
        return this.recuPar;
    }

    public Colisage recuPar(String recuPar) {
        this.setRecuPar(recuPar);
        return this;
    }

    public void setRecuPar(String recuPar) {
        this.recuPar = recuPar;
    }

    public Boolean getEstRecu() {
        return this.estRecu;
    }

    public Colisage estRecu(Boolean estRecu) {
        this.setEstRecu(estRecu);
        return this;
    }

    public void setEstRecu(Boolean estRecu) {
        this.estRecu = estRecu;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Colisage dateCreation(ZonedDateTime dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<Email> getEmails() {
        return this.emails;
    }

    public void setEmails(Set<Email> emails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setColisage(null));
        }
        if (emails != null) {
            emails.forEach(i -> i.setColisage(this));
        }
        this.emails = emails;
    }

    public Colisage emails(Set<Email> emails) {
        this.setEmails(emails);
        return this;
    }

    public Colisage addEmail(Email email) {
        this.emails.add(email);
        email.setColisage(this);
        return this;
    }

    public Colisage removeEmail(Email email) {
        this.emails.remove(email);
        email.setColisage(null);
        return this;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public Colisage personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Colisage)) {
            return false;
        }
        return getId() != null && getId().equals(((Colisage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Colisage{" +
            "id=" + getId() +
            ", destination='" + getDestination() + "'" +
            ", canal='" + getCanal() + "'" +
            ", recuPar='" + getRecuPar() + "'" +
            ", estRecu='" + getEstRecu() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
