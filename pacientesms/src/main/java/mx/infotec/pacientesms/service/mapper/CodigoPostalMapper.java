package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.CodigoPostal;
import mx.infotec.pacientesms.service.dto.CodigoPostalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CodigoPostal} and its DTO {@link CodigoPostalDTO}.
 */
@Mapper(componentModel = "spring")
public interface CodigoPostalMapper extends EntityMapper<CodigoPostalDTO, CodigoPostal> {}
