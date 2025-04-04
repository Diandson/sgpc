package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Colisage;
import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.domain.Production;
import com.m2i.sgpc.service.dto.ColisageDTO;
import com.m2i.sgpc.service.dto.PersonneDTO;
import com.m2i.sgpc.service.dto.ProductionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Production} and its DTO {@link ProductionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductionMapper extends EntityMapper<ProductionDTO, Production> {
    @Mapping(target = "personne", source = "personne", qualifiedByName = "personneId")
    @Mapping(target = "producteur", source = "producteur", qualifiedByName = "personneId")
    @Mapping(target = "receveur", source = "receveur", qualifiedByName = "personneId")
    @Mapping(target = "colisage", source = "colisage", qualifiedByName = "colisageId")
    ProductionDTO toDto(Production s);

    @Named("personneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "user", source = "user")
    PersonneDTO toDtoPersonneId(Personne personne);

    @Named("colisageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "canal", source = "canal")
    ColisageDTO toDtoColisageId(Colisage colisage);
}
