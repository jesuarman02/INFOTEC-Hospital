package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Enfermedad;
import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Enfermedad} and its DTO {@link EnfermedadDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnfermedadMapper extends EntityMapper<EnfermedadDTO, Enfermedad> {}
