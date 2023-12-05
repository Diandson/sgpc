package com.m2i.sgpc.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Personne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonneDTO implements Serializable {

    private Long id;

    private String nom;

    private String prenom;

    private String titre;

    private String numeroDocument;

    private String telephone;

    private FilialeDTO filiale;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumeroDocument() {
        return numeroDocument;
    }

    public void setNumeroDocument(String numeroDocument) {
        this.numeroDocument = numeroDocument;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public FilialeDTO getFiliale() {
        return filiale;
    }

    public void setFiliale(FilialeDTO filiale) {
        this.filiale = filiale;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        PersonneDTO personneDTO = (PersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", titre='" + getTitre() + "'" +
            ", numeroDocument='" + getNumeroDocument() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", filiale=" + getFiliale() +
            ", user=" + getUser() +
            "}";
    }
}
