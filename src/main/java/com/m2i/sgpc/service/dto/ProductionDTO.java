package com.m2i.sgpc.service.dto;

import com.m2i.sgpc.domain.enumeration.ETATPRODUCTION;
import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Production} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductionDTO implements Serializable {

    private Long id;

    private String libelle;

    @Lob
    private byte[] fichier;

    private String fichierContentType;
    private Boolean finish;

    private ETATPRODUCTION etat;

    private String validerPar;

    private LocalDate dateDepot;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private ZonedDateTime dateValider;

    private LocalDate dateOuvert;

    private ZonedDateTime dateCreation;

    private PersonneDTO personne;

    private PersonneDTO producteur;

    private PersonneDTO receveur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public byte[] getFichier() {
        return fichier;
    }

    public void setFichier(byte[] fichier) {
        this.fichier = fichier;
    }

    public String getFichierContentType() {
        return fichierContentType;
    }

    public void setFichierContentType(String fichierContentType) {
        this.fichierContentType = fichierContentType;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

    public ETATPRODUCTION getEtat() {
        return etat;
    }

    public void setEtat(ETATPRODUCTION etat) {
        this.etat = etat;
    }

    public String getValiderPar() {
        return validerPar;
    }

    public void setValiderPar(String validerPar) {
        this.validerPar = validerPar;
    }

    public LocalDate getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ZonedDateTime getDateValider() {
        return dateValider;
    }

    public void setDateValider(ZonedDateTime dateValider) {
        this.dateValider = dateValider;
    }

    public LocalDate getDateOuvert() {
        return dateOuvert;
    }

    public void setDateOuvert(LocalDate dateOuvert) {
        this.dateOuvert = dateOuvert;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public PersonneDTO getPersonne() {
        return personne;
    }

    public void setPersonne(PersonneDTO personne) {
        this.personne = personne;
    }

    public PersonneDTO getProducteur() {
        return producteur;
    }

    public void setProducteur(PersonneDTO producteur) {
        this.producteur = producteur;
    }

    public PersonneDTO getReceveur() {
        return receveur;
    }

    public void setReceveur(PersonneDTO receveur) {
        this.receveur = receveur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductionDTO)) {
            return false;
        }

        ProductionDTO productionDTO = (ProductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductionDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", fichier='" + getFichier() + "'" +
            ", finish='" + getFinish() + "'" +
            ", etat='" + getEtat() + "'" +
            ", validerPar='" + getValiderPar() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", dateValider='" + getDateValider() + "'" +
            ", dateOuvert='" + getDateOuvert() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", personne=" + getPersonne() +
            ", producteur=" + getProducteur() +
            ", receveur=" + getReceveur() +
            "}";
    }
}
