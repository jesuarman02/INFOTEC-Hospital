package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.HistorialMedico;
import mx.infotec.pacientesms.service.dto.HistorialMedicoDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HistorialMedicoMapper extends EntityMapper<HistorialMedicoDTO, HistorialMedico> {

}