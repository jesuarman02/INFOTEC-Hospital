package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.EntidadFederativa;
import mx.infotec.pacientesms.service.dto.EntidadFederativaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EntidadFederativa} and its DTO {@link EntidadFederativaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntidadFederativaMapper extends EntityMapper<EntidadFederativaDTO, EntidadFederativa> {}
