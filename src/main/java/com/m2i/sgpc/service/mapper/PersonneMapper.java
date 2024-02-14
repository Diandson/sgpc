package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Filiale;
import com.m2i.sgpc.domain.Personne;
import com.m2i.sgpc.domain.User;
import com.m2i.sgpc.service.dto.FilialeDTO;
import com.m2i.sgpc.service.dto.PersonneDTO;
import com.m2i.sgpc.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Personne} and its DTO {@link PersonneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonneMapper extends EntityMapper<PersonneDTO, Personne> {
    @Mapping(target = "filiale", source = "filiale", qualifiedByName = "filialeId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    PersonneDTO toDto(Personne s);

    @Named("filialeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "denomination", source = "denomination")
    FilialeDTO toDtoFilialeId(Filiale filiale);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
