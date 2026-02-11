package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.SignosVitales;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import mx.infotec.pacientesms.service.dto.SignosVitalesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SignosVitales} and its DTO {@link SignosVitalesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SignosVitalesMapper extends EntityMapper<SignosVitalesDTO, SignosVitales> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    SignosVitalesDTO toDto(SignosVitales s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);
}
