package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Medicamento;
import mx.infotec.pacientesms.domain.Paciente;
import mx.infotec.pacientesms.domain.PacienteMedicamento;
import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
import mx.infotec.pacientesms.service.dto.PacienteDTO;
import mx.infotec.pacientesms.service.dto.PacienteMedicamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PacienteMedicamento} and its DTO {@link PacienteMedicamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PacienteMedicamentoMapper extends EntityMapper<PacienteMedicamentoDTO, PacienteMedicamento> {
    @Mapping(target = "paciente", source = "paciente", qualifiedByName = "pacienteId")
    @Mapping(target = "medicamento", source = "medicamento", qualifiedByName = "medicamentoNombre")
    PacienteMedicamentoDTO toDto(PacienteMedicamento s);

    @Named("pacienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PacienteDTO toDtoPacienteId(Paciente paciente);

    @Named("medicamentoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MedicamentoDTO toDtoMedicamentoNombre(Medicamento medicamento);
}
