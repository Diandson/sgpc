package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Colisage;
import com.m2i.sgpc.domain.Email;
import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.service.dto.ColisageDTO;
import com.m2i.sgpc.service.dto.EmailDTO;
import com.m2i.sgpc.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Email} and its DTO {@link EmailDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailMapper extends EntityMapper<EmailDTO, Email> {
    @Mapping(target = "colisage", source = "colisage", qualifiedByName = "colisageId")
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    EmailDTO toDto(Email s);

    @Named("colisageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ColisageDTO toDtoColisageId(Colisage colisage);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
