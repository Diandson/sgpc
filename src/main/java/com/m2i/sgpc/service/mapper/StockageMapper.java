package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.domain.Stockage;
import com.m2i.sgpc.service.dto.PersonneDTO;
import com.m2i.sgpc.service.dto.StockageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stockage} and its DTO {@link StockageDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockageMapper extends EntityMapper<StockageDTO, Stockage> {
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    StockageDTO toDto(Stockage s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonneDTO toDtoPersonneId(Personne personne);
}
