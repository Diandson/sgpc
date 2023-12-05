package com.m2i.sgpc.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Filiale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FilialeDTO implements Serializable {

    private Long id;

    private String denomination;

    private String sigle;

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

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilialeDTO)) {
            return false;
        }

        FilialeDTO filialeDTO = (FilialeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filialeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilialeDTO{" +
            "id=" + getId() +
            ", denomination='" + getDenomination() + "'" +
            ", sigle='" + getSigle() + "'" +
            "}";
    }
}
