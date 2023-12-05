package com.m2i.sgpc.service.mapper;

import com.m2i.sgpc.domain.Filiale;
import com.m2i.sgpc.service.dto.FilialeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filiale} and its DTO {@link FilialeDTO}.
 */
@Mapper(componentModel = "spring")
public interface FilialeMapper extends EntityMapper<FilialeDTO, Filiale> {}
