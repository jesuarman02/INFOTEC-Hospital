package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Alergia;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.PacienteAlergia;
import mx.infotec.pacientesms.service.dto.AlergiaDTO;
import mx.infotec.pacientesms.service.dto.PacienteAlergiaDTO;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PacienteAlergia} and its DTO {@link PacienteAlergiaDTO}.
 */
/* */
@Mapper(componentModel = "spring")
public interface PacienteAlergiaMapper extends EntityMapper<PacienteAlergiaDTO, PacienteAlergia> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "alergia", source = "alergia", qualifiedByName = "alergiaNombre")
    PacienteAlergiaDTO toDto(PacienteAlergia s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("alergiaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    AlergiaDTO toDtoAlergiaNombre(Alergia alergia);
}
