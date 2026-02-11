package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.HistorialMedico;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistorialMedico} and its DTO {@link HistorialMedicoDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistorialMedicoMapper extends EntityMapper<HistorialMedicoDTO, HistorialMedico> {}
