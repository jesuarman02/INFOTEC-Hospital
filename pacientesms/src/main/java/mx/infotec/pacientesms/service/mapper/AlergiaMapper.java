package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Alergia;
import mx.infotec.pacientesms.service.dto.AlergiaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Alergia} and its DTO {@link AlergiaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlergiaMapper extends EntityMapper<AlergiaDTO, Alergia> {}
