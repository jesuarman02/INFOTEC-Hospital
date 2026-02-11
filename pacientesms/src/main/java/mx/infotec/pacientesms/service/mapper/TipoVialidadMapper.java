package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.TipoVialidad;
import mx.infotec.pacientesms.service.dto.TipoVialidadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoVialidad} and its DTO {@link TipoVialidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoVialidadMapper extends EntityMapper<TipoVialidadDTO, TipoVialidad> {}
