package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Paciente} and its DTO {@link PacienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMapper extends EntityMapper<PacienteDTO, Paciente> {
    // Al quitar el código de aquí, MapStruct hará la traducción básica automáticamente
    // sin buscar las tablas que ya desconectamos.
}