package com.m2i.sgpc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.m2i.sgpc.domain.enumeration.ETATPRODUCTION;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Production.
 */
@Entity
@Table(name = "production")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Production implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Lob
    @Column(name = "fichier")
    private byte[] fichier;

    @Column(name = "fichier_content_type")
    private String fichierContentType;

    @Column(name = "finish")
    private Boolean finish;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private ETATPRODUCTION etat;

    @Column(name = "valider_par")
    private String validerPar;

    @Column(name = "date_depot")
    private LocalDate dateDepot;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "date_valider")
    private ZonedDateTime dateValider;

    @Column(name = "date_ouvert")
    private LocalDate dateOuvert;

    @Column(name = "date_creation")
    private ZonedDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "productions", "stockages", "colisages", "emails", "filiale", "user", "producteurs", "receveurs" },
        allowSetters = true
    )
    private Personne personne;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "productions", "stockages", "colisages", "emails", "filiale", "user", "producteurs", "receveurs" },
        allowSetters = true
    )
    private Personne producteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "productions", "stockages", "colisages", "emails", "filiale", "user", "producteurs", "receveurs" },
        allowSetters = true
    )
    private Personne receveur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Production id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Production libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public byte[] getFichier() {
        return this.fichier;
    }

    public Production fichier(byte[] fichier) {
        this.setFichier(fichier);
        return this;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return this.fichierContentType;
    }

    public Production fichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
        return this;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Boolean getFinish() {
        return this.finish;
    }

    public Production finish(Boolean finish) {
        this.setFinish(finish);
        return this;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public ETATPRODUCTION getEtat() {
        return this.etat;
    }

    public Production etat(ETATPRODUCTION etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(ETATPRODUCTION etat) {
        this.etat = etat;
    }

    public String getValiderPar() {
        return this.validerPar;
    }

    public Production validerPar(String validerPar) {
        this.setValiderPar(validerPar);
        return this;
    }

    public void setValiderPar(String validerPar) {
        this.validerPar = validerPar;
    }

    public LocalDate getDateDepot() {
        return this.dateDepot;
    }

    public Production dateDepot(LocalDate dateDepot) {
        this.setDateDepot(dateDepot);
        return this;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Production dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Production dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ZonedDateTime getDateValider() {
        return this.dateValider;
    }

    public Production dateValider(ZonedDateTime dateValider) {
        this.setDateValider(dateValider);
        return this;
    }

    public void setDateValider(ZonedDateTime dateValider) {
        this.dateValider = dateValider;
    }

    public LocalDate getDateOuvert() {
        return this.dateOuvert;
    }

    public Production dateOuvert(LocalDate dateOuvert) {
        this.setDateOuvert(dateOuvert);
        return this;
    }

    public void setDateOuvert(LocalDate dateOuvert) {
        this.dateOuvert = dateOuvert;
    }

    public ZonedDateTime getDateCreation() {
        return this.dateCreation;
    }

    public Production dateCreation(ZonedDateTime dateCreation) {
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

    public Production personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    public Personne getProducteur() {
        return this.producteur;
    }

    public void setProducteur(Personne personne) {
        this.producteur = personne;
    }

    public Production producteur(Personne personne) {
        this.setProducteur(personne);
        return this;
    }

    public Personne getReceveur() {
        return this.receveur;
    }

    public void setReceveur(Personne personne) {
        this.receveur = personne;
    }

    public Production receveur(Personne personne) {
        this.setReceveur(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Production)) {
            return false;
        }
        return getId() != null && getId().equals(((Production) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Production{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", fichier='" + getFichier() + "'" +
            ", fichierContentType='" + getFichierContentType() + "'" +
            ", finish='" + getFinish() + "'" +
            ", etat='" + getEtat() + "'" +
            ", validerPar='" + getValiderPar() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", dateValider='" + getDateValider() + "'" +
            ", dateOuvert='" + getDateOuvert() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
