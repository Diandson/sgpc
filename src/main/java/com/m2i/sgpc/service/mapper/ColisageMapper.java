package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Colisage;
import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.service.dto.ColisageDTO;
import com.m2i.sgpc.service.dto.PersonneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Colisage} and its DTO {@link ColisageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColisageMapper extends EntityMapper<ColisageDTO, Colisage> {
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    ColisageDTO toDto(Colisage s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
