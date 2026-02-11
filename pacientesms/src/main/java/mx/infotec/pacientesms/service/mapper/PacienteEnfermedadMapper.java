package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Enfermedad;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.PacienteEnfermedad;
import mx.infotec.pacientesms.service.dto.EnfermedadDTO;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import mx.infotec.pacientesms.service.dto.PacienteEnfermedadDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PacienteEnfermedad} and its DTO {@link PacienteEnfermedadDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteEnfermedadMapper extends EntityMapper<PacienteEnfermedadDTO, PacienteEnfermedad> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "enfermedad", source = "enfermedad", qualifiedByName = "enfermedadNombre")
    PacienteEnfermedadDTO toDto(PacienteEnfermedad s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("enfermedadNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EnfermedadDTO toDtoEnfermedadNombre(Enfermedad enfermedad);
}
