package com.m2i.sgpc.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.m2i.sgpc.domain.Email} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmailDTO implements Serializable {

    private Long id;

    private String objet;

    private String contenu;

    private String destinataire;

    private ZonedDateTime dateEnvoi;

    private ColisageDTO colisage;

    private PersonneDTO personne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public ZonedDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(ZonedDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public ColisageDTO getColisage() {
        return colisage;
    }

    public void setColisage(ColisageDTO colisage) {
        this.colisage = colisage;
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
        if (!(o instanceof EmailDTO)) {
            return false;
        }

        EmailDTO emailDTO = (EmailDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, emailDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmailDTO{" +
            "id=" + getId() +
            ", objet='" + getObjet() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", destinataire='" + getDestinataire() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            ", colisage=" + getColisage() +
            ", personne=" + getPersonne() +
            "}";
    }
}
