package com.m2i.sgpc.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Colisage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ColisageDTO implements Serializable {

    private Long id;

    private String destination;

    private String canal;

    private String recuPar;

    private Boolean estRecu;

    private ZonedDateTime dateCreation;

    private PersonneDTO personne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getRecuPar() {
        return recuPar;
    }

    public void setRecuPar(String recuPar) {
        this.recuPar = recuPar;
    }

    public Boolean getEstRecu() {
        return estRecu;
    }

    public void setEstRecu(Boolean estRecu) {
        this.estRecu = estRecu;
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
        if (!(o instanceof ColisageDTO)) {
            return false;
        }

        ColisageDTO colisageDTO = (ColisageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, colisageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ColisageDTO{" +
            "id=" + getId() +
            ", destination='" + getDestination() + "'" +
            ", canal='" + getCanal() + "'" +
            ", recuPar='" + getRecuPar() + "'" +
            ", estRecu='" + getEstRecu() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", personne=" + getPersonne() +
            "}";
    }
}
