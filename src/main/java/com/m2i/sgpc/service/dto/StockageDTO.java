package com.m2i.sgpc.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Stockage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockageDTO implements Serializable {

    private Long id;

    private String denomination;

    private String code;

    private String quantite;

    private String modifierPar;

    private ZonedDateTime dateCreation;

    private PersonneDTO personne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantite() {
        return quantite;
    }

    public void setQuantite(String quantite) {
        this.quantite = quantite;
    }

    public String getModifierPar() {
        return modifierPar;
    }

    public void setModifierPar(String modifierPar) {
        this.modifierPar = modifierPar;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockageDTO)) {
            return false;
        }

        StockageDTO stockageDTO = (StockageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockageDTO{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", code='" + getCode() + "'" +
            ", quantite='" + getQuantite() + "'" +
            ", modifierPar='" + getModifierPar() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", personne=" + getPersonne() +
            "}";
    }
}
