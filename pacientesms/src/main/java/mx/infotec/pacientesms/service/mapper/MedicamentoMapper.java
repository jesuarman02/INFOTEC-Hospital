package mx.infotec.pacientesms.service.mapper;

import mx.infotec.pacientesms.domain.Medicamento;
import mx.infotec.pacientesms.service.dto.MedicamentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicamento} and its DTO {@link MedicamentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicamentoMapper extends EntityMapper<MedicamentoDTO, Medicamento> {}
